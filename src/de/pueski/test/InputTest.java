package de.pueski.test;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

public class InputTest {

	static Controller defaultController = null;
	
	
	public static void main(String[] args) throws Exception {
		Controllers.create();		
		Controllers.poll();
		
		for (int i = 0; i < Controllers.getControllerCount();i++) {
			Controller controller = Controllers.getController(i);			
			if (controller.getName().startsWith("XBOX")) {
				defaultController = controller;				
			}
		}
		
		
		do {
			Controllers.poll();
			
			System.out.println("rx : "+defaultController.getXAxisValue());
			System.out.println("ry : "+defaultController.getYAxisValue());
		
			
			for (int i = 0; i < defaultController.getButtonCount();i++) {
				System.out.println("Button "+i+" "+defaultController.isButtonPressed(i));
			}
			
			
			
		} while(true);
		
	}
	
}
