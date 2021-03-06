package net.sf.anathema.character.generic.data;

import net.sf.anathema.lib.resources.ResourceFile;

public interface IExtensibleDataSetCompiler {
	String getName();
	
	String getRecognitionPattern();
	
	void registerFile(ResourceFile resource) throws Exception;
	
	IExtensibleDataSet build();
}