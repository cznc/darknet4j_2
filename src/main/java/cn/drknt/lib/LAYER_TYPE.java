package cn.drknt.lib;

/**
 * 
 * */
public enum LAYER_TYPE {
	CONVOLUTIONAL("convolutional"),
    DECONVOLUTIONAL("deconvolutional"),
    CONNECTED("connected"),
    MAXPOOL("maxpool"),
    SOFTMAX("softmax"),
    DETECTION("detection"),
    DROPOUT("dropout"),
    CROP("crop"),
    ROUTE("route"),
    COST("cost"),//is it out layer? see get_network_output_layer()
    NORMALIZATION("normalization"),
    AVGPOOL("avgpool"),
    LOCAL("local"),
    SHORTCUT("shortcut"),
    ACTIVE("activation"),
    RNN("rnn"),
    GRU("gru"),
    CRNN("crnn"),
    BATCHNORM("batchnorm"),
    NETWORK("network"),//
    XNOR("xnor"),//
    REGION("region"),
    REORG("reorg"),
    BLANK("blank"),//
	LSTM("lstm");////
	private String name; 
	private LAYER_TYPE(String name) {  
        this.name = name;  
    }
	public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }
}
