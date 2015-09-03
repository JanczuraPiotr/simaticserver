package pjpl.s7.process;

import java.util.HashMap;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
abstract public class Process implements Runnable{


	protected HashMap<Integer, pjpl.s7.device.PLC> devices = new HashMap<>();
}
