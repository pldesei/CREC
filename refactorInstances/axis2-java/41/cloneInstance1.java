    public void testInnerParticle32() {

        TestInnerParticle3 testInnerParticle3 = new TestInnerParticle3();
        TestInnerParticle3Choice_type0 testInnerParticle3Choice_type1 = new TestInnerParticle3Choice_type0();
        testInnerParticle3Choice_type1.setParam2("Param2");
        testInnerParticle3Choice_type1.setParam3("Param3");
        testInnerParticle3.setTestInnerParticle3Choice_type0(testInnerParticle3Choice_type1);

        try {
            OMElement omElement =
                    testInnerParticle3.getOMElement(TestInnerParticle3.MY_QNAME, OMAbstractFactory.getOMFactory());
            String omElementString = omElement.toStringWithConsume();
            System.out.println("OM String ==> " + omElementString);
            XMLStreamReader xmlReader = StAXUtils.createXMLStreamReader(new ByteArrayInputStream(omElementString.getBytes()));
            TestInnerParticle3 result = TestInnerParticle3.Factory.parse(xmlReader);
            assertEquals(result.getTestInnerParticle3Choice_type0().getParam3(), "Param3");
        } catch (XMLStreamException e) {
            e.printStackTrace();
            fail();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
