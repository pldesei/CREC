            public void forEachValueInDoc(int docId, ValueInDocProc proc) {
                Ordinals.Docs.Iter iter = ordinals.getIter(docId);
                int ord = iter.next();
                if (ord == 0) {
                    proc.onMissing(docId);
                    return;
                }
                do {
                    bytes.fill(scratch, termOrdToBytesOffset.get(ord));
                    proc.onValue(docId, scratch);
                } while ((ord = iter.next()) != 0);
            }
