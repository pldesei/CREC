                            for (int j = 0; j < value.length; j++) {
                                Object o = value[j];
                                if (elemntNameSpace != null) {
                                    object.add(new QName(elemntNameSpace.getNamespaceURI(),
                                                         propDesc.getName(),
                                                         elemntNameSpace.getPrefix()));
                                } else {
                                    object.add(new QName(beanName.getNamespaceURI(),
                                                         propDesc.getName(), beanName.getPrefix()));
                                }
                                object.add(o);
                            }
