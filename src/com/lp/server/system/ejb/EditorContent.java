package com.lp.server.system.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.EditorBaseBlockDto;
import com.lp.server.system.service.EditorContentDto;
import com.lp.server.util.EditorContentIId;

@Entity(name = "LP_EDITOR_CONTENT")
public class EditorContent {
	@Id
	@Column(name = "I_ID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "editor_content_id")
	@TableGenerator(name = "editor_content_id", table = "LP_PRIMARYKEY", pkColumnName = "C_NAME", pkColumnValue = PKConst.PK_EDITOR_CONTENT, valueColumnName = "I_INDEX", allocationSize = 10)
	private Integer id;

	@Column(name = "LOCALE_C_NR")
	private String locale;
	
	@OneToMany(mappedBy = "editorContent", cascade = CascadeType.ALL)
	private List<EditorBaseBlock> blocks = new ArrayList<EditorBaseBlock>();

	public EditorContentIId getId() {
		return new EditorContentIId(id);
	}

	public void setId(EditorContentIId id) {
		this.id = id.id();
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	protected EditorContent() {
	}

	public EditorContent(EditorContentIId id, String locale) {
		this.id = id.id();
		this.locale = locale;
	}
	
	public List<EditorBaseBlock> getBlocks() {
		return blocks;
	}

	public void addBlock(EditorBaseBlock block) {
		blocks.add(block);
		block.setEditorContent(this);
	}
	
	public void removeBlock(EditorBaseBlock block) {
		blocks.remove(block);
		block.setEditorContent(null);
	}
	
	public static EditorContent fromDto(EditorContentDto dto) {
		EditorContent content = new EditorContent(dto.getId(), dto.getLocale());
		for(EditorBaseBlockDto blockDto : dto.getBlockDtos()) {
			EditorBaseBlock block = EditorBaseBlock.fromDto(blockDto);
			content.addBlock(block);
		}
		return content;
	}

}
