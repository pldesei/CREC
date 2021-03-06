            if ((values.length >= 1) && (values[0] != null)) {
                AxisConfiguration registry =
                        configurationContext.getAxisConfiguration();

                AxisService axisService = registry.getService(values[0]);

                // If the axisService is not null we get the binding that the request came to add
                // add it as a property to the messageContext
                if (axisService != null) {
                    Map endpoints = axisService.getEndpoints();
                    if (endpoints != null) {
                        if (endpoints.size() == 1) {
                            messageContext.setProperty(WSDL2Constants.ENDPOINT_LOCAL_NAME,
                                                       endpoints.get(
                                                               axisService.getEndpointName()));
                        } else {
                            String endpointName = values[0].substring(values[0].indexOf(".") + 1);
                            messageContext.setProperty(WSDL2Constants.ENDPOINT_LOCAL_NAME,
                                                       endpoints.get(endpointName));
                        }
                    }
                }

                return axisService;
            } else {
