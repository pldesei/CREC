    public void testEditBean() throws AxisFault {
        configureSystem("editBean");

        Options options = new Options();
        options.setTransportInProtocol(Constants.TRANSPORT_HTTP);

        ConfigurationContext configContext =
                ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
        RPCServiceClient sender = new RPCServiceClient(configContext, null);
        sender.setOptions(options);
        options.setTo(targetEPR);

        MyBean bean = new MyBean();
        bean.setAge(100);
        bean.setName("Deepal");
        bean.setValue(false);
        AddressBean ab = new AddressBean();
        ab.setNumber(1010);
        ab.setTown("Colombo3");
        bean.setAddress(ab);


        ArrayList args = new ArrayList();
        args.add(bean);
        args.add("159");

        OMElement response = sender.invokeBlocking(operationName, args.toArray());
        MyBean resBean = (MyBean)BeanUtil.deserialize(MyBean.class, response.getFirstElement(),
                                                      new DefaultObjectSupplier(), null);
        assertNotNull(resBean);
        assertEquals(resBean.getAge(), 159);
    }
