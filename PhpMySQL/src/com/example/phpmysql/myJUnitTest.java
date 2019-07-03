package com.example.phpmysql;

import junit.framework.*;

public class myJUnitTest extends TestCase {

	final static String RetornoEsperado = "testando"; 
	final static String RetornoFeito = "testando";
	
	public static void main(String[] args) {
		assertEquals (RetornoEsperado, RetornoFeito, 0);
	}
	
	public void sampleTest(){
		assertEquals (RetornoEsperado, RetornoFeito, 0);
	}

}
