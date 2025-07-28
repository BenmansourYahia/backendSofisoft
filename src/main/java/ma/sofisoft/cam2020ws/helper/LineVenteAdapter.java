package ma.sofisoft.cam2020ws.helper;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class LineVenteAdapter<T> extends TypeAdapter<T> {

	@Override
	public void write(JsonWriter writer, T value) throws IOException {
		// TODO Auto-generated method stub
		if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(value.toString());
	}

	@Override
	public T read(JsonReader in) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
