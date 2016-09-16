package com.chenlb.mmseg4j.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MaxWordSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.SimpleSeg;

/**
 * MMSeg分词器<br/>
 * 分词模式mode可选配置项：<br/>
 * simple/complex/maxwrod,默认为maxword模式
 *
 */
public class MMSegAnalyzer extends Analyzer {
	public static final String MODE_SIMPLE = "simple";
	public static final String MODE_COMPLEX = "complex";
	public static final String MODE_MAXWORD = "maxword";
	
	/**分词器内置需要加载的默认字典文件*/
	protected Dictionary dic;

	/**自定义停用词加载路径*/
	protected String stopwordsPath;
	/**分词模式：simple/complex/maxwrod,默认为maxword模式*/
	protected String mode;

	public MMSegAnalyzer() {
		dic = Dictionary.getInstance();
	}

	public MMSegAnalyzer(String mode) {
		this.mode = mode;
	}

	public MMSegAnalyzer(String dicPath,String mode) {
		buildDic(dicPath);
		this.mode = mode;
	}
	
	public MMSegAnalyzer(String dicPath,String mode,String stopwordsPath) {
		buildDic(dicPath);
		this.stopwordsPath = stopwordsPath;
		this.mode = mode;
	}
	
	public MMSegAnalyzer(File dicPath,String mode,String stopwordsPath) {
		dic = Dictionary.getInstance(dicPath);
		this.stopwordsPath = stopwordsPath;
		this.mode = mode;
	}
	
	public MMSegAnalyzer(File dicPath,String mode) {
		dic = Dictionary.getInstance(dicPath);
		this.mode = mode;
	}

	public MMSegAnalyzer(File dicPath) {
		dic = Dictionary.getInstance(dicPath);
	}
	
	public MMSegAnalyzer(Dictionary dic,String mode,String stopwordsPath) {
		this.dic = dic;
		this.stopwordsPath = stopwordsPath;
		this.mode = mode;
	}
	
	public MMSegAnalyzer(Dictionary dic,String mode) {
		this.dic = dic;
		this.mode = mode;
	}

	public MMSegAnalyzer(Dictionary dic) {
		this.dic = dic;
	}

	protected Seg newSeg() {
		Seg seg = null;
		if(MODE_SIMPLE.equalsIgnoreCase(mode)) {
			seg = new SimpleSeg(dic);
		} else if(MODE_COMPLEX.equalsIgnoreCase(mode)) {
			seg = new ComplexSeg(dic);
		} else if(MODE_MAXWORD.equalsIgnoreCase(mode)) {
			seg = new MaxWordSeg(dic);
		} else {
			seg = new MaxWordSeg(dic);
		}
		return seg;
	}

	public Dictionary getDict() {
		return dic;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer tokenizer = null;
		if(null != stopwordsPath && !"".equals(stopwordsPath)) {
			tokenizer = new MMSegTokenizer(newSeg(),stopwordsPath);
		} else {
			tokenizer = new MMSegTokenizer(newSeg());
		}
		return new TokenStreamComponents(tokenizer);
	}
	
	private void buildDic(String path) {
		if(path == null || "".equals(path)) {
			this.dic = Dictionary.getInstance();
		} else {
			this.dic = Dictionary.getInstance(path);
		}
	}
}
