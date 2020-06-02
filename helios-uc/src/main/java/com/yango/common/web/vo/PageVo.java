package com.yango.common.web.vo;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class PageVo<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private int pageNo = 1;// 当前页
	private int pageSize = 10;// 页的大小(默认显示10条)
	private int total;// 总共记录数
	private int totalPage; // 总页数

	private List<T> rows = Lists.newArrayList();

	public PageVo(int pageNo, int pageSize, int total) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.total = total;
		this.totalPage = countTotalPage(pageSize, total);
	}

	public <S> PageVo(PageVo<S> pageVo, Function<S, T> mapperFunc) {
		this.pageNo = pageVo.getPageNo();
		this.pageSize = pageVo.getPageSize();
		this.total = pageVo.getTotal();

		this.rows = pageVo.rows.stream().map(mapperFunc).collect(Collectors.toList());
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public int getPageNo() {
		return pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotal() {
		return total;
	}

	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * 计算总页数
	 * 
	 * @param pageSize
	 * @param totalRow
	 * @return
	 */
	public int countTotalPage(int pageSize, int total) {
		int totalPage = total % pageSize == 0 ? (total / pageSize) : (total / pageSize) + 1;
		return totalPage;
	}
}
