package com.tabjy.dining_philosophers;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Chopstick {

	private static int PICK_UP_TIME_OUT;
	public static int ID_INC = 0;

	private final int id = ID_INC++;
	private final ReentrantLock lock = new ReentrantLock();
	private Philosopher pickedUpBy;

	public static void reset() {
		ID_INC = 0;
	}

	public void lock() {
		lock.lock();
	}

	public boolean pickUp(Philosopher philosopher, long timeout) throws InterruptedException {
		if (!lock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
			return false;
		}

		pickedUpBy = philosopher;
		return true;
	}

	public void putDown() {
		lock.unlock();
		pickedUpBy = null;
	}

	@Override
	public String toString() {
		return "Chopstick{" + "id=" + id + ", pickedUpBy=" + pickedUpBy + '}';
	}
}
