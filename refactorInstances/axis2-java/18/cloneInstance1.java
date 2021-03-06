    public void testMulitrefBean() throws Exception {
        configureSystem("editBean");
        OMFactory fac = OMAbstractFactory.getOMFactory();

        OMNamespace omNs = fac.createOMNamespace(NAMESPACE, "my");
        OMElement method = fac.createOMElement("editBean", omNs);
        OMElement value = fac.createOMElement("arg0", null);
        value.addAttribute(fac.createOMAttribute("href", null, "#1"));
        method.addChild(value);
        OMElement value2 = fac.createOMElement("arg1", null);
        value2.setText("159");
        method.addChild(value2);


        SOAPFactory factory = OMAbstractFactory.getSOAP11Factory();
        SOAPEnvelope envelope = factory.getDefaultEnvelope();
        envelope.getBody().addChild(method);


        String ref1 =
                "<reference id=\"1\"><name>Deepal</name><value href=\"#2\"/><address href=\"#3\"/></reference>";
        OMElement om1 = getOMElement(ref1, fac);
        envelope.getBody().addChild(om1);
        String ref2 = "<reference id=\"2\">false</reference>";
        OMElement om2 = getOMElement(ref2, fac);
        envelope.getBody().addChild(om2);
        String ref3 = "<reference id=\"3\"><town href=\"#4\"/><number>1010</number></reference>";
        OMElement om3 = getOMElement(ref3, fac);
        envelope.getBody().addChild(om3);
        String ref4 = "<reference id=\"4\">Colombo3</reference>";
        OMElement om4 = getOMElement(ref4, fac);
        envelope.getBody().addChild(om4);

        Options options = new Options();
        options.setTo(targetEPR);
        options.setTransportInProtocol(Constants.TRANSPORT_HTTP);

        ConfigurationContext configContext = ConfigurationContextFactory
                .createConfigurationContextFromFileSystem(null, null);
        RPCServiceClient rpcClient = new RPCServiceClient(configContext, null);
        rpcClient.setOptions(options);
        MessageContext reqMessageContext = configContext.createMessageContext();
        OperationClient opClinet = rpcClient.createClient(ServiceClient.ANON_OUT_IN_OP);
        opClinet.setOptions(options);
        reqMessageContext.setEnvelope(envelope);

        opClinet.addMessageContext(reqMessageContext);
        opClinet.execute(true);

        MessageContext responseMessageContx =
                opClinet.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);

        SOAPEnvelope env = responseMessageContx.getEnvelope();

        OMElement response = env.getBody().getFirstElement();
        MyBean resBean = (MyBean)BeanUtil.deserialize(MyBean.class, response.getFirstElement(),
                                                      new DefaultObjectSupplier(), null);
        assertNotNull(resBean);
        assertEquals(resBean.getAge(), 159);
    }
