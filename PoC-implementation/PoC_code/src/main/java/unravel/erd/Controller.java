package unravel.erd;

import java.util.UUID;

import unravel.misc.Identifiable;

/**
 *
 */
public abstract class Controller implements Identifiable {

	// attributes
	final private String uuid;

	public Controller() {
		uuid = UUID.randomUUID().toString();
	}

	@Override
	public String getUuid() {
		return uuid;
	}

	public abstract int hashCode();
	public abstract boolean equals(Object obj);
}