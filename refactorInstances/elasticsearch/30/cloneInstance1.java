    protected void doStart() throws ElasticsearchException {
        gateway.start();
        // if we received initial state, see if we can recover within the start phase, so we hold the
        // node from starting until we recovered properly
        if (discoveryService.initialStateReceived()) {
            ClusterState clusterState = clusterService.state();
            DiscoveryNodes nodes = clusterState.nodes();
            if (clusterState.nodes().localNodeMaster() && clusterState.blocks().hasGlobalBlock(STATE_NOT_RECOVERED_BLOCK)) {
                if (clusterState.blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK)) {
                    logger.debug("not recovering from gateway, no master elected yet");
                } else if (recoverAfterNodes != -1 && (nodes.masterAndDataNodes().size()) < recoverAfterNodes) {
                    logger.debug("not recovering from gateway, nodes_size (data+master) [" + nodes.masterAndDataNodes().size() + "] < recover_after_nodes [" + recoverAfterNodes + "]");
                } else if (recoverAfterDataNodes != -1 && nodes.dataNodes().size() < recoverAfterDataNodes) {
                    logger.debug("not recovering from gateway, nodes_size (data) [" + nodes.dataNodes().size() + "] < recover_after_data_nodes [" + recoverAfterDataNodes + "]");
                } else if (recoverAfterMasterNodes != -1 && nodes.masterNodes().size() < recoverAfterMasterNodes) {
                    logger.debug("not recovering from gateway, nodes_size (master) [" + nodes.masterNodes().size() + "] < recover_after_master_nodes [" + recoverAfterMasterNodes + "]");
                } else {
                    boolean ignoreRecoverAfterTime;
                    if (expectedNodes == -1 && expectedMasterNodes == -1 && expectedDataNodes == -1) {
                        // no expected is set, don't ignore the timeout
                        ignoreRecoverAfterTime = false;
                    } else {
                        // one of the expected is set, see if all of them meet the need, and ignore the timeout in this case
                        ignoreRecoverAfterTime = true;
                        if (expectedNodes != -1 && (nodes.masterAndDataNodes().size() < expectedNodes)) { // does not meet the expected...
                            ignoreRecoverAfterTime = false;
                        }
                        if (expectedMasterNodes != -1 && (nodes.masterNodes().size() < expectedMasterNodes)) { // does not meet the expected...
                            ignoreRecoverAfterTime = false;
                        }
                        if (expectedDataNodes != -1 && (nodes.dataNodes().size() < expectedDataNodes)) { // does not meet the expected...
                            ignoreRecoverAfterTime = false;
                        }
                    }
                    performStateRecovery(ignoreRecoverAfterTime);
                }
            }
        } else {
            logger.debug("can't wait on start for (possibly) reading state from gateway, will do it asynchronously");
        }
        clusterService.addLast(this);
    }
