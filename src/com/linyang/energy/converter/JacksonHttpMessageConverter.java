package com.linyang.energy.converter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.JavaType;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.Assert;

/**
 * 
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 2, 2013
 */
public class JacksonHttpMessageConverter extends AbstractHttpMessageConverter<Object> {
	
	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	
	private ObjectMapper objectMapper =  new ObjectMapper();
	private boolean prefixJson = false;
	
	/**
	 * Construct a new {@code BindingJacksonHttpMessageConverter}.
	 */
	public JacksonHttpMessageConverter() {
		super(new MediaType("application", "json", DEFAULT_CHARSET));
		// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
		//对于特殊符号入单引号的处理
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		//设置遇到时间格式Date的处理
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * Set the {@code ObjectMapper} for this view. If not set, a default
	 * {@link ObjectMapper#ObjectMapper() ObjectMapper} is used.
	 * <p>Setting a custom-configured {@code ObjectMapper} is one way to take further control of the JSON
	 * serialization process. For example, an extended {@link org.codehaus.jackson.map.SerializerFactory}
	 * can be configured that provides custom serializers for specific types. The other option for refining
	 * the serialization process is to use Jackson's provided annotations on the types to be serialized,
	 * in which case a custom-configured ObjectMapper is unnecessary.
	 */
	public void setObjectMapper(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "ObjectMapper must not be null");
		this.objectMapper = objectMapper;
	}
	
	/**
	 * Return the underlying {@code ObjectMapper} for this view.
	 */
	public ObjectMapper getObjectMapper() {
		return this.objectMapper;
	}

	/**
	 * Indicate whether the JSON output by this view should be prefixed with "{} &&". Default is false.
	 * <p>Prefixing the JSON string in this manner is used to help prevent JSON Hijacking.
	 * The prefix renders the string syntactically invalid as a script so that it cannot be hijacked.
	 * This prefix does not affect the evaluation of JSON, but if JSON validation is performed on the
	 * string, the prefix would need to be ignored.
	 */
	public void setPrefixJson(boolean prefixJson) {
		this.prefixJson = prefixJson;
	}
	
	
	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		JavaType javaType = getJavaType(clazz);
		return (this.objectMapper.canDeserialize(javaType) && canRead(mediaType));
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return (this.objectMapper.canSerialize(clazz) && canWrite(mediaType));
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		// should not be called, since we override canRead/Write instead
		throw new UnsupportedOperationException();
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		JavaType javaType = getJavaType(clazz);
		try {
			return this.objectMapper.readValue(inputMessage.getBody(), javaType);
		}
		catch (IOException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}

	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
		JsonGenerator jsonGenerator =
				this.objectMapper.getJsonFactory().createJsonGenerator(outputMessage.getBody(), encoding);
		try {
			if (this.prefixJson) {
				jsonGenerator.writeRaw("{} && ");
			}
			this.objectMapper.writeValue(jsonGenerator, object);
		}
		catch (IOException ex) {
			throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
		}
	}
	
	/**
	 * Return the Jackson {@link JavaType} for the specified class.
	 * <p>The default implementation returns {@link ObjectMapper#constructType(java.lang.reflect.Type)},
	 * but this can be overridden in subclasses, to allow for custom generic collection handling.
	 * For instance:
	 * <pre class="code">
	 * protected JavaType getJavaType(Class&lt;?&gt; clazz) {
	 *   if (List.class.isAssignableFrom(clazz)) {
	 *     return objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, MyBean.class);
	 *   } else {
	 *     return super.getJavaType(clazz);
	 *   }
	 * }
	 * </pre>
	 * @param clazz the class to return the java type for
	 * @return the java type
	 */
	protected JavaType getJavaType(Class<?> clazz) {
		return objectMapper.constructType(clazz);
	}

	/**
	 * Determine the JSON encoding to use for the given content type.
	 * @param contentType the media type as requested by the caller
	 * @return the JSON encoding to use (never <code>null</code>)
	 */
	protected JsonEncoding getJsonEncoding(MediaType contentType) {
		if (contentType != null && contentType.getCharSet() != null) {
			Charset charset = contentType.getCharSet();
			for (JsonEncoding encoding : JsonEncoding.values()) {
				if (charset.name().equals(encoding.getJavaName())) {
					return encoding;
				}
			}
		}
		return JsonEncoding.UTF8;
	}

}
