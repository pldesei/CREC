    public void testSerialization() throws IOException, ClassNotFoundException {
        LogarithmicAxis a1 = new LogarithmicAxis("Test Axis");
        LogarithmicAxis a2;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(a1);
        out.close();

        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(
                buffer.toByteArray()));
        a2 = (LogarithmicAxis) in.readObject();
        in.close();
        assertEquals(a1, a2);
    }
