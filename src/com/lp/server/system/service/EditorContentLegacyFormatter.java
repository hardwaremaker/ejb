package com.lp.server.system.service;

public class EditorContentLegacyFormatter implements EditorContentFormatter<String> {

	@Override
	public String formatContent(EditorContentDto content) {
		StringBuilder sb = new StringBuilder();
		for (EditorBaseBlockDto block : content.getBlockDtos()) {
			if (block instanceof EditorTextBlockDto) {
				sb.append(((EditorTextBlockDto) block).getText());
			}
		}
		return sb.toString();
	}

}
