package spikes.sneer.pulp.brickmanager.impl;

import static sneer.commons.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import sneer.brickness.PublicKey;
import sneer.commons.lang.exceptions.NotImplementedYet;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.log.Logger;
import sneer.pulp.reactive.collections.MapRegister;
import sneer.pulp.reactive.collections.MapSignal;
import sneer.pulp.reactive.collections.CollectionSignals;
import spikes.sneer.kernel.container.ContainerOld;
import spikes.sneer.kernel.container.SneerConfig;
import spikes.sneer.pulp.brickmanager.BrickManager;
import spikes.sneer.pulp.brickmanager.BrickManagerException;
import spikes.sneer.pulp.dependency.DependencyManager;
import spikes.sneer.pulp.dependency.FileWithHash;
import spikes.sneer.pulp.deployer.BrickBundle;
import spikes.sneer.pulp.deployer.BrickFile;

class BrickManagerImpl implements BrickManager {

	private final SneerConfig _config = my(SneerConfig.class);

	private final DependencyManager _dependencyManager = my(DependencyManager.class);
	
	private final KeyManager _keyManager = my(KeyManager.class);
	
	private final MapRegister<String, BrickFile> _bricksByName = my(CollectionSignals.class).newMapRegister();

	@Override
	public void install(BrickBundle bundle) {
		
		/*
		 * Must install brick on the right order, otherwise the runOnceOnInstall 
		 * will fail because the brick dependencies will not be found on the filesystem.
		 * sorting will fail if dependency cycles are found
		 */
		bundle.sort(); 
		
		List<String> brickNames = bundle.brickNames();
		for (String brickName : brickNames) {
			BrickFile brick = bundle.brick(brickName);
			
			if(!okToInstall(brick))
				throw new BrickManagerException("brick: "+brickName+" could not be installed");
			
			resolve(bundle, brick);
			install(brick);
		}
	}

	private void resolve(BrickFile brick) {
		resolve(null, brick);
	}
	
	private void resolve(BrickBundle bundle, BrickFile brick) {
		Iterable<String> brickDependencies;
		try {
			brickDependencies = brick.brickDependencies();
		} catch (IOException e) {
			throw new BrickManagerException("Error searching for injected bricks on "+brick.name(), e);
		}
		for (String dependency : brickDependencies) {
			BrickFile inBundle = bundle == null ? null : bundle.brick(dependency); 
			if(inBundle == null) { 
				//not inBudle, try local registry
				inBundle = brick(dependency);
				if(inBundle == null) {
					//not found. must ask other peer via network
					
					throw new NotImplementedYet();
					//BrickFile justGotten = retrieveRemoteBrick(brick.origin(), injected.brickName());
					//install(justGotten);
				}
			}
		}
		brick.resolved(true);
	}

	@Override
	public BrickFile brick(String brickName) {
		return _bricksByName.output().currentGet(brickName);
	}

	private boolean okToInstall(BrickFile brick) {
		String brickName = brick.name();
		BrickFile installed = brick(brickName);
		if(installed == null)
			return true;
		
		//compare hashes
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public void install(BrickFile brick) throws BrickManagerException {
		String brickName = brick.name();
		log("Installing brick: "+brickName);
		
		//0. resolve injected Bricks
		if(!brick.resolved())
			resolve(brick);

		//1. create brick directory under sneer home
		File brickDirectory = setUpBrickDirectory(brickName);
		
		//2. copy received files
		BrickFile installed = copyBrickFiles(brick, brickDirectory);
		
		//3. install dependencies
		copyDependencies(brick, installed);

		//4. update origin. When origin is null we are installing the brick locally, not using meToo
		PublicKey origin = brick.origin();
		origin = origin != null ? origin : _keyManager.ownPublicKey(); 
		installed.origin(origin);
		
		//5. give the brick a chance to initialize itself (register menus, etc)
		runOnceOnInstall(installed);
		
		_bricksByName.put(brickName, installed);
	}
	
	private File setUpBrickDirectory(String brickName) {
		File brickDirectory = brickDirectory(brickName);
		log("installing "+brickName+" on "+brickDirectory);
		
		if(brickDirectory.exists()) {
			log("cleaning");
			tryToCleanDirectory(brickDirectory); //FixUrgent: ask permission to overwrite?
		} else { 
			boolean ok = brickDirectory.mkdir();
			log("creating: " + ok);
		}
		return brickDirectory;
	}

	private void log(String message) {
		my(Logger.class).log(message);
	}

	private void tryToCleanDirectory(File brickDirectory) {
		try {
			FileUtils.cleanDirectory(brickDirectory);
		} catch (IOException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}

	private void runOnceOnInstall(BrickFile installed) {
		Class<?> clazz = resolveBrickInterface(installed);
		my(ContainerOld.class).provide(clazz);
	}

	private Class<?> resolveBrickInterface(BrickFile installed) {
		try {
			return my(ContainerOld.class).resolve(installed.name());
		} catch (ClassNotFoundException e) {
			throw new BrickManagerException(e.getMessage(), e);
		}
	}

	private void copyDependencies(BrickFile brick, BrickFile installed) {
		String brickName = brick.name();
		List<FileWithHash> brickDependencies = brick.fileDependencies();
		for (FileWithHash dependency : brickDependencies) {
			try {
				dependency = _dependencyManager.add(brickName, dependency);
				installed.fileDependencies().add(dependency);
			} catch (IOException e) {
				throw new BrickManagerException("Error installing dependecy: "+dependency, e);
			}
		}
	}

	private BrickFile copyBrickFiles(BrickFile brick, File brickDirectory) {
		BrickFile installed;
		try {
			installed = brick.copyTo(brickDirectory);
		} catch (IOException e) {
			throw new BrickManagerException("Error copying brick files to: "+brickDirectory);
		}
		return installed;
	}
	
	private File brickDirectory(String brickName) {
		File root = _config.brickRootDirectory();
		File brickDirectory = new File(root, brickName);
		return brickDirectory;
	}

	@Override
	public MapSignal<String, BrickFile> bricks() {
		return _bricksByName.output();
	}
}