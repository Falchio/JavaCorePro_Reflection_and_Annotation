package application;

import test.AfterSuite;
import test.BeforeSuite;
import test.Test;

public class Calculator {

    @BeforeSuite
    public int beforeSuite(int a, int b){
        return a*b*a;
    }

    @AfterSuite
    public int afterSuite(int a, int b){
        return a+b*a;
    }

    @Test(priority = 10)
    public int addition(int a, int b) {
        return a + b;
    }

    @Test(priority = 5)
    public int multiply(int a, int b) {
        return a * b;
    }

    @Test
    public int division(int a, int b) {
        return a / b;
    }

    @Test(priority = 2)
    public int subtraction(int a, int b) {
        return a - b;
    }

    @Test(priority = 1)
    public int exponentiation(int a, int b) {
        return (int) Math.pow(a, b);
    }

    public int notForTest(int a, int b) {
        return a + b * (a - 1);
    }


    public int notForTest2(int a, int b) {
        return a + b * (a - 1);
    }
}
