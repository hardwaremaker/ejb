package com.lp.server.system.ejb;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.lp.server.system.service.EditorBaseBlockDto;
import com.lp.server.system.service.EditorTextBlockDto;
import com.lp.server.util.EditorBlockIId;

@Entity(name = "LP_EDITOR_TEXT_BLOCK")
public class EditorTextBlock extends EditorBaseBlock {
	@Column(name = "TEXT")
	private String text;

	protected EditorTextBlock() {
	}

	public EditorTextBlock(EditorBlockIId iid) {
		super(iid);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public EditorBaseBlockDto toDto() {
		return new EditorTextBlockDto(getRow(), getColumn(), getText()).setId(getIid());
	}

	@Override
	protected void loadFromDto(EditorBaseBlockDto dto) {
		super.loadFromDto(dto);
		EditorTextBlockDto textDto = (EditorTextBlockDto) dto;
		setText(textDto.getText());
	}
}
