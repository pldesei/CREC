    public void testInnerParticle33() {

        TestInnerParticle3 testInnerParticle3 = new TestInnerParticle3();
        testInnerParticle3.setParam1("Param1");
        TestInnerParticle3Choice_type0 testInnerParticle3Choice_type1 = new TestInnerParticle3Choice_type0();
        testInnerParticle3Choice_type1.setParam2("Param2");
        testInnerParticle3Choice_type1.setParam3("Param3");
        testInnerParticle3.setTestInnerParticle3Choice_type0(testInnerParticle3Choice_type1);
        testInnerParticle3.setParam4("Param4");

         try {
             OMElement omElement =
                      testInnerParticle3.getOMElement(TestInnerParticle3.MY_QNAME, OMAbstractFactory.getOMFactory());
            String omElementString = omElement.toStringWithConsume();
            System.out.println("OM String ==> " + omElementString);
            XMLStreamReader xmlReader = StAXUtils.createXMLStreamReader(new ByteArrayInputStream(omElementString.getBytes()));
            TestInnerParticle3 result = TestInnerParticle3.Factory.parse(xmlReader);
            assertEquals(result.getParam4(), "Param4");
        } catch (XMLStreamException e) {
            fail();
        } catch (Exception e) {
            fail();
        }

    }
