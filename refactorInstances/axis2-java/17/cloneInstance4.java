    public void testaddError() {
        try {
            configureSystem("add");
            OMFactory fac = OMAbstractFactory.getOMFactory();

            OMNamespace omNs = fac.createOMNamespace(NAMESPACE, "my");
            OMElement method = fac.createOMElement("add", omNs);
            OMElement value = fac.createOMElement("arg0", null);
            value.addAttribute(fac.createOMAttribute("href", null, "#1"));
            method.addChild(value);

            OMElement value2 = fac.createOMElement("arg1", null);
            value2.addAttribute(fac.createOMAttribute("href", null, "#2"));
            method.addChild(value2);

            SOAPFactory factory = OMAbstractFactory.getSOAP11Factory();
            SOAPEnvelope envelope = factory.getDefaultEnvelope();
            envelope.getBody().addChild(method);

            OMElement ref = fac.createOMElement("reference", null);
            ref.addAttribute(fac.createOMAttribute("id", null, "1"));
            ref.setText("10");
            envelope.getBody().addChild(ref);

            OMElement ref2 = fac.createOMElement("reference", null);
            ref2.addAttribute(fac.createOMAttribute("id", null, "3"));
            ref2.setText("10");
            envelope.getBody().addChild(ref2);

            Options options = new Options();
            options.setTo(targetEPR);
            options.setTransportInProtocol(Constants.TRANSPORT_HTTP);

            ConfigurationContext configContext = ConfigurationContextFactory
                    .createConfigurationContextFromFileSystem(null, null);
            RPCServiceClient rpcClient = new RPCServiceClient(configContext, null);
            rpcClient.setOptions(options);
            MessageContext reqMessageContext = configContext.createMessageContext();;
            OperationClient opClinet = rpcClient.createClient(ServiceClient.ANON_OUT_IN_OP);
            opClinet.setOptions(options);
            reqMessageContext.setEnvelope(envelope);

            opClinet.addMessageContext(reqMessageContext);
            opClinet.execute(true);

            fail("This should fail with : " + "org.apache.axis2.AxisFault: Invalid reference :2");
        } catch (AxisFault axisFault) {
            String val = axisFault.getFaultDetailElement().toString();
            System.out.println("val = " + val);
            int index = val.indexOf("org.apache.axis2.AxisFault: Invalid reference :2");
            if (index < 0) {
                fail("This should fail with : " +
                        "org.apache.axis2.AxisFault: Invalid reference :2");
            }
        }
    }
