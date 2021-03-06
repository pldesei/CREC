            int width, int height, ChartRenderingInfo info) throws IOException {

        if (file == null) {
            throw new IllegalArgumentException("Null 'file' argument.");
        }
        if (chart == null) {
            throw new IllegalArgumentException("Null 'chart' argument.");
        }
        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        try {
            writeChartAsJPEG(out, chart, width, height, info);
        }
        finally {
            out.close();
        }

    }
