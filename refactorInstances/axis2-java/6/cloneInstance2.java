    public void testFaultsService2(){
        //FaultyWebServiceFault_Exception exception = null;
        Exception exception = null;
        try{
            System.out.println("----------------------------------");
            System.out.println("test: " + getName());
            FaultsService service = new FaultsService();
            FaultsServicePortType proxy = service.getFaultsPort();
            BindingProvider p = (BindingProvider)proxy;
            p.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,axisEndpoint);

            // the invoke will throw an exception, if the test is performed right
            int total = proxy.throwFault(2, "Complex", 2);  // "Complex" will cause service to throw ComplexFault_Exception
            
        }catch(BaseFault_Exception e){
            fail("Should not get BaseFault_Exception in this testcase");
        } catch (ComplexFault_Exception e) {
            exception = e;
        }
        
        System.out.println("----------------------------------");
        
        assertNotNull(exception);
        assertTrue(((ComplexFault_Exception)exception).getFaultInfo() instanceof ComplexFault);
        
    }
