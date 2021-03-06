                    while (jClassIter.hasNext()) {
                        JClass jclass = (JClass) jClassIter.next();
                        if (jclass.getQualifiedName().equals(className)) {
                            /**
                             * Schema genertaion done in two stage 1. Load all the methods and
                             * create type for methods parameters (if the parameters are Bean
                             * then it will create Complex types for those , and if the
                             * parameters are simple type which decribe in SimpleTypeTable
                             * nothing will happen) 2. In the next stage for all the methods
                             * messages and port types will be creteated
                             */
                            JAnnotation annotation =
                                    jclass.getAnnotation(AnnotationConstants.WEB_SERVICE);
                            if (annotation != null) {
                                Class claxx = Class.forName(
                                        "org.apache.axis2.jaxws.description.DescriptionFactory");
                                Method mthod = claxx.getMethod(
                                        "createAxisService",
                                        new Class[]{Class.class});
                                Class pojoClass = Loader.loadClass(classLoader, className);
                                AxisService axisService =
                                        (AxisService) mthod.invoke(claxx, new Object[]{pojoClass});
                                Utils.fillAxisService(axisService,
                                                      configCtx.getAxisConfiguration(),
                                                      new ArrayList(),
                                                      new ArrayList());
                                axisService.setName(className);
                                setMessageReceivers(axisService);
                                axisServiceList.add(axisService);
                            }
                        }
                    }
