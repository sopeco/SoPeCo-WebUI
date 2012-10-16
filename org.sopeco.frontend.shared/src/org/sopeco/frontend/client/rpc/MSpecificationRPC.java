package org.sopeco.frontend.client.rpc;

import java.util.List;

import org.sopeco.persistence.entities.definition.MeasurementSpecification;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * 
 * @author Marius Oehler
 * 
 */
@RemoteServiceRelativePath("mSpecificationRPC")
public interface MSpecificationRPC extends RemoteService {

	/**
	 * Return a list with all specification names, of the selected scenario.
	 * 
	 * @return list with names
	 */
	List<String> getAllSpecificationNames();

	/**
	 * Return a list with all specifications.
	 * 
	 * @return list with names
	 */
	List<MeasurementSpecification> getAllSpecifications();

	/**
	 * Set the current working specification to the given specification.
	 * 
	 * @param specificationName
	 * @return
	 */
	boolean setWorkingSpecification(String specificationName);

	/**
	 * Creates a new specification with the given name.
	 * 
	 * @param name
	 * @return
	 */
	boolean createSpecification(String name);

	/**
	 * Renames the working specification to the new name.
	 * 
	 * @param newName
	 *            the new name
	 * @return
	 */
	boolean renameWorkingSpecification(String newName);
}
