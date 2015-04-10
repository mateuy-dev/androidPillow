package cat.my.android.pillow.data.rest;
public class Route{
	int method;
	String url;
	public Route(int method, String url) {
		super();
		this.method = method;
		this.url = url;
	}
	public int getMethod() {
		return method;
	}
	public void setMethod(int method) {
		this.method = method;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}