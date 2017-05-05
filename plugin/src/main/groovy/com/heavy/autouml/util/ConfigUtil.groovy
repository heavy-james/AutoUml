package com.heavy.autouml.util

public class ConfigUtil {

	public static final String ROOT_DIR = System.getProperty("user.dir");
	public static final String CONFIG_DIR = ROOT_DIR + File.separator + "config";

	public static Properties getConfig(String configFileName) {
		File configFile = new File(CONFIG_DIR + File.separator + configFileName);
		if (configFile.exists() && configFile.isFile()) {
			InputStream configStream = null;
			try {
				configStream = new FileInputStream(configFile);
				Properties pros = new Properties();
				pros.load(configStream);
				return pros;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (configStream != null) {
					try {
						configStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	public static String readConfig(String configFileName, String key) {
		Properties props = getConfig(configFileName);
		if (props != null) {
			return props.getProperty(key);
		}
		return null;
	}

	public static void writeConfig(String configFileName, Properties properties) {
		OutputStream output = null;

		try {
			output = new FileOutputStream("config.properties");
			properties.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public static void updateConfig(String configFileName, Properties properties) {
		Properties pros = getConfig(configFileName);
		if (pros != null && properties != null) {
			Set<Object> keys = properties.keySet();
			Iterator<Object> iter = keys.iterator();
			while (iter.hasNext()) {
				Object keyOjb = iter.next();
				pros.put(keyOjb, properties.get(keyOjb));
			}
			writeConfig(configFileName, pros);
		}

	}

	public static String[] getValues(String splitSymbol, String value) {
		if (value != null && splitSymbol != null && !"".equals(value)) {
			return value.split(splitSymbol);
		}
		return null;
	}

}
