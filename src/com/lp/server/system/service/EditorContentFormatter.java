package com.lp.server.system.service;

public interface EditorContentFormatter<T> {
	T formatContent(EditorContentDto content);
}
