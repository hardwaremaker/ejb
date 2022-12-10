package com.lp.server.system.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.lp.server.system.ejb.EditorBaseBlock;
import com.lp.server.system.ejb.EditorContent;
import com.lp.server.system.service.EditorBaseBlockDto;
import com.lp.server.system.service.EditorContentDto;

public class EditorContentDtoAssembler {
	public static EditorContentDto assembleEditorContentDto(EditorContent content) {
		EditorContentDto dto = new EditorContentDto(content.getId(), content.getLocale(),
				assembleEditorBlockDtos(content.getBlocks()));
		return dto;
	}
	
	public static List<EditorContentDto> assembleEditorContentDtos(Collection<EditorContent> contents) {
		return contents.stream().map(EditorContentDtoAssembler::assembleEditorContentDto).collect(Collectors.toList());
	}

	public static EditorBaseBlockDto assembleEditorBlockDto(EditorBaseBlock block) {
		return block.toDto();
	}

	public static List<EditorBaseBlockDto> assembleEditorBlockDtos(Collection<EditorBaseBlock> blocks) {
		return blocks.stream().map(EditorContentDtoAssembler::assembleEditorBlockDto).collect(Collectors.toList());
	}
}
