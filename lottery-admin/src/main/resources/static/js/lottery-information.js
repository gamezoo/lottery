var lotteryInformationVM = new Vue({
	el : '#lottery-information',
	data : {
		showLotteryInformationFlag : true,
		source : '',
		title : '',

		informationActionTitle : '',
		informationContentEditor : null,
		addOrUpdateInformationFlag : false,
		editInformation : {},

		showViewInformationFlag : false,
		viewInformation : {},

		showInformationCrawlerFlag : false,
		informationCrawlers : [],
		addOrUpdateCrawlerFlag : false,
		crawlerActionTitle : '',
		editCrawler : {},

		showCollectionInformationFlag : false,
		selectedCrawler : {},
		informations : [],
		selectedInformation : {}
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.initTable();
		this.informationContentEditor = new window.wangEditor('#informationContentEditor');
		this.informationContentEditor.customConfig.menus = [ 'head', // 标题
		'bold', // 粗体
		'fontSize', // 字号
		'fontName', // 字体
		'italic', // 斜体
		'underline', // 下划线
		'strikeThrough', // 删除线
		'foreColor', // 文字颜色
		'backColor', // 背景颜色
		'link', // 插入链接
		'list', // 列表
		'justify', // 对齐方式
		'quote', // 引用
		'emoticon', // 表情
		'table', // 表格
		'code', // 插入代码
		'undo', // 撤销
		'redo' // 重复
		];
		this.informationContentEditor.create();
	},
	methods : {

		syncInformation : function() {
			var that = this;
			that.$http.post('/lotteryInformation/syncInformation', that.informations).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.showCollectionInformationFlag = false;
				that.showLotteryInformationFlag = true;
				that.refreshTable();
			});
		},

		collectionInformation : function(crawler) {
			var that = this;
			that.informations = [];
			that.selectedInformation = null;
			that.selectedCrawler = crawler;
			that.showInformationCrawlerFlag = false;
			that.showCollectionInformationFlag = true;
			layer.msg('正在采集资讯...', {
				icon : 16,
				shade : 0.01
			});
			that.$http.get('/lotteryInformation/collectionInformation', {
				params : {
					id : crawler.id
				}
			}).then(function(res) {
				that.informations = res.body.data;
				layer.closeAll();
			});
		},

		addOrUpdateCrawler : function() {
			var that = this;
			var editCrawler = that.editCrawler;
			if (editCrawler.source == null || editCrawler.source == '') {
				layer.alert('请输入来源', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			editCrawler.script = this.codeMirror.getDoc().getValue();
			if (editCrawler.script == null || editCrawler.script == '') {
				layer.alert('请输入脚本', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/lotteryInformation/addOrUpdateInformationCrawler', editCrawler).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateCrawlerFlag = false;
				that.toInformationCrawlerPage();
			});
		},

		showAddCrawlerModal : function() {
			this.addOrUpdateCrawlerFlag = true;
			this.crawlerActionTitle = '新增爬虫';
			this.editCrawler = {
				source : '',
				script : ''
			};
			this.initCodeMirror();
		},

		showEditCrawlerModal : function(id) {
			var that = this;
			that.$http.get('/lotteryInformation/findInformationCrawlerById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.editCrawler = res.body.data;
				that.addOrUpdateCrawlerFlag = true;
				that.crawlerActionTitle = '编辑爬虫';
				that.initCodeMirror();
			});
		},

		initCodeMirror : function() {
			this.$nextTick(function() {
				if (this.codeMirror != null) {
					this.codeMirror.toTextArea();
					this.codeMirror = null;
				}
				this.codeMirror = CodeMirror.fromTextArea($('.crawler-script')[0], {
					lineNumbers : true,
					lineWrapping : true
				});
				this.codeMirror.getDoc().setValue(this.editCrawler.script);
			});
		},

		delCrawler : function(id) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/lotteryInformation/delInformationCrawlerById', {
					params : {
						id : id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.toInformationCrawlerPage();
				});
			});
		},

		toInformationCrawlerPage : function() {
			var that = this;
			that.$http.get('/lotteryInformation/findAllInformationCrawler').then(function(res) {
				that.showLotteryInformationFlag = false;
				that.showInformationCrawlerFlag = true;
				that.informationCrawlers = res.body.data;
			});
		},

		backToInformationCrawlerPage : function() {
			this.showCollectionInformationFlag = false;
			this.toInformationCrawlerPage();
		},

		showAddInformationModal : function() {
			this.addOrUpdateInformationFlag = true;
			this.informationActionTitle = '新增资讯';
			this.editInformation = {
				title : '',
				content : '',
				source : '',
				publishTime : ''
			};
			this.informationContentEditor.txt.html('');
		},

		showEditInformationModal : function(id) {
			var that = this;
			that.$http.get('/lotteryInformation/findInformationById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.editInformation = res.body.data;
				that.addOrUpdateInformationFlag = true;
				that.informationActionTitle = '编辑资讯';
				this.informationContentEditor.txt.html(that.editInformation.content);
			});
		},

		addOrUpdateInformation : function() {
			var that = this;
			var editInformation = that.editInformation;
			if (editInformation.source == null || editInformation.source == '') {
				layer.alert('请输入来源', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editInformation.title == null || editInformation.title == '') {
				layer.alert('请输入标题', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editInformation.publishTime == null || editInformation.publishTime == '') {
				layer.alert('请输入发布时间', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var content = this.informationContentEditor.txt.html();
			if (content == null || content == '') {
				layer.alert('请输入内容', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			editInformation.content = content;
			that.$http.post('/lotteryInformation/addOrUpdateInformation', editInformation).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateInformationFlag = false;
				that.refreshTable();
			});
		},

		delInformation : function(id) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/lotteryInformation/delInformationById', {
					params : {
						id : id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.refreshTable();
				});
			});
		},

		showViewInformationModal : function(information) {
			this.showViewInformationFlag = true;
			this.viewInformation = information;
		},

		backToInformationPage : function() {
			this.showInformationCrawlerFlag = false;
			this.showLotteryInformationFlag = true;
		},

		initTable : function() {
			var that = this;
			$('.lottery-information-table').bootstrapTable({
				classes : 'table table-hover',
				height : 490,
				url : '/lotteryInformation/findLotteryInformationByPage',
				pagination : true,
				sidePagination : 'server',
				pageNumber : 1,
				pageSize : 10,
				pageList : [ 10, 25, 50, 100 ],
				queryParamsType : '',
				queryParams : function(params) {
					var condParam = {
						pageSize : params.pageSize,
						pageNum : params.pageNumber,
						source : that.source,
						title : that.title
					};
					return condParam;
				},
				responseHandler : function(res) {
					return {
						total : res.data.total,
						rows : res.data.content
					};
				},
				columns : [ {
					field : 'source',
					title : '来源'
				}, {
					field : 'title',
					title : '资讯标题'
				}, {
					field : 'publishTime',
					title : '发布时间'
				}, {
					title : '操作',
					formatter : function(value, row, index) {
						return [ '<button type="button" class="view-information-btn btn btn-outline-primary btn-sm" style="margin-right: 4px;">查看内容</button>', '<button type="button" class="edit-information-btn btn btn-outline-success btn-sm" style="margin-right: 4px;">编辑</button>', '<button type="button" class="del-information-btn btn btn-outline-danger btn-sm">删除</button>' ].join('');
					},
					events : {
						'click .view-information-btn' : function(event, value, row, index) {
							that.showViewInformationModal(row);
						},
						'click .edit-information-btn' : function(event, value, row, index) {
							that.showEditInformationModal(row.id);
						},
						'click .del-information-btn' : function(event, value, row, index) {
							that.delInformation(row.id);
						}
					}
				} ]
			});
		},

		refreshTable : function() {
			$('.lottery-information-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},
	}
});