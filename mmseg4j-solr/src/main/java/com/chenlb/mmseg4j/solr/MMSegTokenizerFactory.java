package com.chenlb.mmseg4j.solr;

import java.util.Map;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MaxWordSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.SimpleSeg;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;

public class MMSegTokenizerFactory extends TokenizerFactory implements ResourceLoaderAware {
	static final Logger log = Logger.getLogger(MMSegTokenizerFactory.class.getName());
	private Dictionary dic = null;
	/**自定义停用词加载路径*/
	protected String stopwordsPath;

	public MMSegTokenizerFactory(Map<String, String> args) {
		super(args);
		this.stopwordsPath = get(args, "stopwordsPath");;
	}

	private Seg newSeg(Map<String, String> args) {
		Seg seg = null;
		String mode = args.get("mode");
		if("simple".equalsIgnoreCase(mode)) {
			log.info("use simple mode");
			seg = new SimpleSeg(dic);
		} else if("complex".equalsIgnoreCase(mode)) {
			log.info("use complex mode");
			seg = new ComplexSeg(dic);
		} else if("maxword".equalsIgnoreCase(mode)) {
			log.info("use max-word mode");
			seg = new MaxWordSeg(dic);
		} else {
			log.info("use max-word mode as default.");
			seg = new MaxWordSeg(dic);
		}
		return seg;
	}

	@Override
	public void inform(ResourceLoader loader) {
		String dicPath = getOriginalArgs().get("dicPath");
		dic = Utils.getDict(dicPath, loader);
		log.info("loading dictionary ... in path [" + dic.getDicPath().toURI() + "]");
	}

	@Override
	public Tokenizer create(AttributeFactory factory) {
		if(null == stopwordsPath || "".equals(stopwordsPath)) {
			return new MMSegTokenizer(factory,newSeg(getOriginalArgs()));
		}
		return new MMSegTokenizer(factory,newSeg(getOriginalArgs()),stopwordsPath);
	}
}
