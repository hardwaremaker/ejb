package com.lp.server.system.ejb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.EditorBaseBlockDto;
import com.lp.server.system.service.EditorImageBlockDto;
import com.lp.server.system.service.EditorTextBlockDto;
import com.lp.server.util.EditorBlockIId;
import com.lp.util.EJBExceptionLP;

@Entity(name = "LP_EDITOR_BASE_BLOCK")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class EditorBaseBlock {
	@Id
	@Column(name = "I_ID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "editor_block_id")
	@TableGenerator(name = "editor_block_id", table = "LP_PRIMARYKEY", pkColumnName = "C_NAME", pkColumnValue = PKConst.PK_EDITOR_BLOCK, valueColumnName = "I_INDEX", allocationSize = 10)
	private Integer id;

	@Column(name = "ROW", nullable = false)
	private int row;

	@Column(name = "COL", nullable = false)
	private int column;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EDITOR_CONTENT_ID")
	private EditorContent editorContent;

	public EditorBlockIId getIid() {
		return new EditorBlockIId(id);
	}

	public EditorBaseBlock(EditorBlockIId id) {
		this.id = id.id();
	}

	protected EditorBaseBlock() {
	}

	public void setIid(EditorBlockIId id) {
		this.id = id.id();
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public EditorContent getEditorContent() {
		return editorContent;
	}

	protected void setEditorContent(EditorContent editorContent) {
		this.editorContent = editorContent;
	}

	public abstract EditorBaseBlockDto toDto();

	/**
	 * Lade Daten aus einem Dto. <br>
	 * MUSS von Subklassen &uuml;berschieben werden, wenn neue Datenfelder definiert
	 * sind!
	 * 
	 * @param dto
	 */
	protected void loadFromDto(EditorBaseBlockDto dto) {
		setIid(dto.getId());
		setRow(dto.getRow());
		setColumn(dto.getColumn());
	}

	/**
	 * Erstellt ein Datenbank Entity Objekt aus dem Dto, beachtet polymorphische
	 * Klassenhierarchie
	 * 
	 * @param dto
	 * @return
	 */
	public static EditorBaseBlock fromDto(EditorBaseBlockDto dto) {
		EditorBaseBlock block;
		if (dto.getClass() == EditorTextBlockDto.class) {
			block = new EditorTextBlock();
		} else if (dto.getClass() == EditorImageBlockDto.class) {
			block = new EditorImageBlock();
		} else {
			throw new EJBExceptionLP(new Exception("Unbekannte Subklasse von EditorBaseBlockDto: " + dto.getClass()
					+ "; Die fromDto Methode muss angepasst werden!"));
		}
		block.loadFromDto(dto);
		return block;
	}
	
}
