var gameManage = new Vue({
	el : '#game-manage',
	data : {
		tabWithGameCategoryId : '',
		gameCategoryDicts : [],
		games : [],
		showGameManageFlag : true,
		selectedGame : {},

		/**
		 * 游戏类别维护start
		 */
		gameCategorys : [],
		showGameCategoryFlag : false,
		selectedGameCategory : {},

		/**
		 * 新增/编辑游戏类别start
		 */
		editGameCategory : {},
		addOrUpdateGameCategoryFlag : false,
		gameCategoryActionTitle : '',

		/**
		 * 新增/编辑游戏start
		 */
		editGame : {},
		addOrUpdateGameFlag : false,
		gameActionTitle : '',

		/**
		 * 字典同步start
		 */
		dictSyncFlag : false,
		syncGameDict : true,
		syncGamePlayDict : true,

		/**
		 * 期号设置start
		 */
		issueSettingFlag : false,
		issueSettingDetails : {},

		/**
		 * 游戏玩法start
		 */
		showGamePlayFlag : false,
		selectedGamePlay : {},
		editGamePlay : {},
		addOrUpdateGamePlayFlag : false,
		gamePlayActionTitle : ''
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.loadGameCategoryDict();
		this.initGameManageTable();
	},
	methods : {

		/**
		 * 初始化表格
		 */
		initGameManageTable : function() {
			var that = this;
			that.$http.get('/game/findGameByGameCategoryId', {
				params : {
					gameCategoryId : that.tabWithGameCategoryId
				}
			}).then(function(res) {
				that.games = res.body.data;
			});
		},

		switchGameCateogry : function(gameCategoryId) {
			this.tabWithGameCategoryId = gameCategoryId;
			this.initGameManageTable();
		},

		toGameCategoryPage : function() {
			var that = this;
			that.$http.get('/game/findAllGameCategory', {}).then(function(res) {
				that.gameCategorys = res.body.data;
				that.showGameManageFlag = false;
				that.showGameCategoryFlag = true;
			});
		},

		loadGameCategoryDict : function() {
			var that = this;
			that.$http.get('/game/findAllGameCategory', {}).then(function(res) {
				that.gameCategoryDicts = res.body.data;
			});
		},

		openAddGameCategoryModal : function() {
			this.addOrUpdateGameCategoryFlag = true;
			this.gameCategoryActionTitle = '新增游戏类别';
			this.editGameCategory = {
				gameCategoryName : '',
				gameCategoryCode : '',
				orderNo : ''
			};
		},

		delGameCategory : function(gameCategory) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/game/delGameCategoryById', {
					params : {
						id : gameCategory.id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.toGameCategoryPage();
				});
			});
		},

		openEditGameCategoryModal : function(gameCategory) {
			var that = this;
			that.$http.get('/game/findGameCategoryById', {
				params : {
					id : gameCategory.id
				}
			}).then(function(res) {
				that.editGameCategory = res.body.data;
				that.addOrUpdateGameCategoryFlag = true;
				that.gameCategoryActionTitle = '编辑游戏类别';
			});
		},

		addOrUpdateGameCategory : function() {
			var that = this;
			var editGameCategory = that.editGameCategory;
			if (editGameCategory.gameCategoryName == null || editGameCategory.gameCategoryName == '') {
				layer.alert('请输入游戏类别', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGameCategory.gameCategoryCode == null || editGameCategory.gameCategoryCode == '') {
				layer.alert('请输入游戏类别code', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGameCategory.orderNo == null || editGameCategory.orderNo == 0) {
				layer.alert('请输入排序号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}

			that.$http.post('/game/addOrUpdateGameCategory', editGameCategory).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateGameCategoryFlag = false;
				that.toGameCategoryPage();
			});
		},

		backToGameManageWithGameCategory : function() {
			this.showGameCategoryFlag = false;
			this.showGameManageFlag = true;
			this.tabWithGameCategoryId = '';
			this.loadGameCategoryDict();
			this.initGameManageTable();
		},

		openIssueSettingModal : function(game) {
			var that = this;
			that.$http.get('/issue/getIssueSettingDetailsByGameId', {
				params : {
					gameId : game.id
				}
			}).then(function(res) {
				if (res.body.data != null) {
					that.issueSettingDetails = res.body.data;
				} else {
					that.issueSettingDetails = {
						gameId : game.id,
						dateFormat : '',
						issueFormat : '',
						issueGenerateRules : []
					};
				}
				that.issueSettingFlag = true;
			});
		},

		addIssueGenerateRule : function(index) {
			this.issueSettingDetails.issueGenerateRules.push({
				startTime : '',
				timeInterval : '',
				issueCount : ''
			});
		},

		delIssueGenerateRule : function(index) {
			this.issueSettingDetails.issueGenerateRules.splice(index, 1);
		},

		/**
		 * 新增或修改期号设置
		 */
		addOrUpdateIssueSetting : function() {
			var that = this;
			var issueSettingDetails = that.issueSettingDetails;
			if (issueSettingDetails.dateFormat == null || issueSettingDetails.dateFormat == '') {
				layer.alert('请输入日期格式', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (issueSettingDetails.issueFormat == null || issueSettingDetails.issueFormat == '') {
				layer.alert('请输入期号格式', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (issueSettingDetails.issueGenerateRules == null || issueSettingDetails.issueGenerateRules.length == 0) {
				layer.alert('请设置期号规则', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			for (var i = 0; i < issueSettingDetails.issueGenerateRules.length; i++) {
				var issueGenerateRule = issueSettingDetails.issueGenerateRules[i];
				if (issueGenerateRule.startTime == null || issueGenerateRule.startTime == '') {
					layer.alert('请输入开始时间', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (issueGenerateRule.timeInterval == null || issueGenerateRule.timeInterval == '') {
					layer.alert('请输入时间间隔', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (issueGenerateRule.issueCount == null || issueGenerateRule.issueCount == '') {
					layer.alert('请输入期数', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
			}

			that.$http.post('/issue/addOrUpdateIssueSetting', issueSettingDetails).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.issueSettingFlag = false;
				that.initGameManageTable();
			});
		},

		openDictSyncModal : function() {
			this.dictSyncFlag = true;
			this.syncGameDict = true;
			this.syncGamePlayDict = true;
		},

		dictSync : function() {
			var that = this;
			that.$http.get('/game/dictSync', {
				params : {
					syncGameDict : that.syncGameDict,
					syncGamePlayDict : that.syncGamePlayDict
				}
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.dictSyncFlag = false;
				that.initGameManageTable();
			});
		},

		openAddGameModal : function() {
			this.addOrUpdateGameFlag = true;
			this.gameActionTitle = '新增游戏';
			this.editGame = {
				gameCategoryId : '',
				gameName : '',
				gameCode : '',
				gameDesc : '',
				hotGameFlag : false,
				state : '1',
				orderNo : '',
				copyGameCode : ''
			};
		},

		delGame : function(game) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/game/delGameById', {
					params : {
						id : game.id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.initGameManageTable();
				});
			});
		},

		openEditGameModal : function(game) {
			var that = this;
			that.$http.get('/game/findGameById', {
				params : {
					id : game.id
				}
			}).then(function(res) {
				that.editGame = res.body.data;
				that.addOrUpdateGameFlag = true;
				that.gameActionTitle = '编辑游戏';
			});
		},

		/**
		 * 新增或修改游戏
		 */
		addOrUpdateGame : function() {
			var that = this;
			var editGame = that.editGame;
			if (editGame.gameCategoryId == null || editGame.gameCategoryId == '') {
				layer.alert('请选择游戏类别', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGame.gameName == null || editGame.gameName == '') {
				layer.alert('请输入游戏名称', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGame.gameCode == null || editGame.gameCode == '') {
				layer.alert('请输入游戏代码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGame.hotGameFlag == null) {
				layer.alert('请选择是否作为热门游戏', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGame.state == null || editGame.state == '') {
				layer.alert('请选择状态', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGame.orderNo == null || editGame.orderNo == '') {
				layer.alert('请选择排序号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/game/addOrUpdateGame', editGame).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateGameFlag = false;
				that.initGameManageTable();
			});
		},

		toGamePlaySetting : function(game) {
			this.selectedGame = game;
			this.loadGamePlayAndInitTree();
		},

		loadGamePlayAndInitTree : function() {
			var that = this;
			this.$http.get('/game/findGamePlayByGameCode', {
				params : {
					gameCode : that.selectedGame.gameCode
				}
			}).then(function(res) {
				var gamePlayCategorys = [];
				var gamePlayCategoryMap = new Map();
				var result = res.body.data;
				for (var i = 0; i < result.length; i++) {
					var obj = result[i];
					var gamePlay = {
						id : obj.id,
						gamePlayCode : obj.gamePlayCode,
						gamePlayName : obj.gamePlayName,
						gamePlayDesc : obj.gamePlayDesc,
						odds : obj.odds,
						numLocates : obj.numLocates,
						gameCode : obj.gameCode,
						gameName : obj.gameName,
						gamePlayCategoryCode : obj.gamePlayCategoryCode,
						gamePlayCategoryName : obj.gamePlayCategoryName,
						subGamePlayCategoryCode : obj.subGamePlayCategoryCode,
						subGamePlayCategoryName : obj.subGamePlayCategoryName,
						state : obj.state,
						stateName : obj.stateName
					};
					var gamePlayCategoryName = gamePlay.gamePlayCategoryName;
					var subGamePlayCategoryName = gamePlay.subGamePlayCategoryName;
					if (gamePlayCategoryMap.get(gamePlayCategoryName) == null) {
						gamePlayCategoryMap.set(gamePlayCategoryName, {});
					}
					var gamePlayCategory = gamePlayCategoryMap.get(gamePlayCategoryName);
					if (gamePlayCategory[subGamePlayCategoryName] == null) {
						gamePlayCategory[subGamePlayCategoryName] = {};
					}
					gamePlayCategory[subGamePlayCategoryName][gamePlay.gamePlayName] = gamePlay;
				}
				var nodes = [];
				gamePlayCategoryMap.forEach(function(value, key, map) {
					var categoryNode = {
						name : key,
						children : []
					};
					for ( var subCategoryKey in value) {
						var subCategoryNode = {
							name : subCategoryKey,
							children : []
						};
						var gamePlays = value[subCategoryKey];
						for ( var gamePlayKey in gamePlays) {
							var gamePlayNode = {
								name : gamePlayKey,
								extObj : gamePlays[gamePlayKey]
							};
							subCategoryNode.children.push(gamePlayNode);
						}
						categoryNode.children.push(subCategoryNode);
					}
					nodes.push(categoryNode);
				});

				that.showGameManageFlag = false;
				that.showGamePlayFlag = true;
				$.fn.zTree.init($('#gamePlayTree'), {
					callback : {
						onClick : function(event, treeId, treeNode, clickFlag) {
							if (treeNode.extObj) {
								that.loadGamePlayDetailsData(treeNode.extObj.id);
							}
						}
					}
				}, nodes);
			});
		},

		backToGameManageWithGamePlay : function() {
			this.showGamePlayFlag = false;
			this.showGameManageFlag = true;
			this.selectedGamePlay = {};
		},

		loadGamePlayDetailsData : function(id) {
			var that = this;
			that.$http.get('/game/findGamePlayDetailsById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.selectedGamePlay = res.body.data;
			});
		},

		updateGamePlayState : function() {
			var that = this;
			if (that.selectedGamePlay == null || that.selectedGamePlay.id == null) {
				return;
			}
			that.$http.get('/game/updateGamePlayState', {
				params : {
					id : that.selectedGamePlay.id,
					state : that.selectedGamePlay.state == '1' ? '0' : '1'
				}
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.loadGamePlayDetailsData(that.selectedGamePlay.id);
			});
		},

		delGamePlay : function() {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/game/delGamePlayById', {
					params : {
						id : that.selectedGamePlay.id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.selectedGamePlay = {};
					that.loadGamePlayAndInitTree();
				});
			});
		},

		cancelEditGamePlay : function() {
			this.addOrUpdateGamePlayFlag = false;
		},

		openEditGamePlayModal : function() {
			var that = this;
			that.addOrUpdateGamePlayFlag = true;
			that.gamePlayActionTitle = '编辑玩法';
			that.$http.get('/game/findGamePlayDetailsById', {
				params : {
					id : that.selectedGamePlay.id
				}
			}).then(function(res) {
				var editGamePlay = res.body.data;
				for (var i = 0; i < editGamePlay.numLocates.length; i++) {
					editGamePlay.numLocates[i].showDetails = false;
				}
				that.editGamePlay = editGamePlay;
			});
		},

		/**
		 * 新增玩法
		 */
		openAddGamePlayModal : function() {
			this.addOrUpdateGamePlayFlag = true;
			this.gamePlayActionTitle = '新增玩法';
			var treeObj = $.fn.zTree.getZTreeObj('gamePlayTree');
			treeObj.cancelSelectedNode();
			this.editGamePlay = {
				gamePlayName : '',
				odds : '',
				gamePlayCategoryName : '',
				subGamePlayCategoryName : '',
				state : '1',
				gameCode : this.selectedGame.gameCode,
				orderNo : '',
				gamePlayDesc : '',
				numLocates : []
			};
		},

		addNumLocate : function() {
			this.editGamePlay.numLocates.push({
				numLocateName : '',
				nums : '',
				maxSelected : '',
				hasFilterBtnFlag : true,
				optionalNums : []
			});
		},

		delNumLocate : function(index) {
			this.editGamePlay.numLocates.splice(index, 1);
		},

		generateNum : function(numLocate, odds) {
			if (numLocate.nums != null && numLocate.nums != '') {
				var optionalNums = [];
				var numSplit = numLocate.nums.split(',');
				for (var i = 0; i < numSplit.length; i++) {
					optionalNums.push({
						num : numSplit[i],
						odds : odds
					});
				}
				numLocate.optionalNums = optionalNums;
			} else {
				numLocate.optionalNums = [];
			}
		},

		generateNumAndShowNumDetails : function(numLocate) {
			numLocate.showDetails = true;
			this.generateNum(numLocate, '');
		},

		/**
		 * 显示号码明细
		 */
		showNumDetails : function(numLocate) {
			numLocate.showDetails = !numLocate.showDetails;
			if (this.editGamePlay.oddsMode == '2' && numLocate.optionalNums.length == 0) {
				if (numLocate.showDetails) {
					this.generateNum(numLocate, '');
				}
			}
		},

		addOrUpdateGamePlay : function() {
			var that = this;
			var editGamePlay = that.editGamePlay;
			if (editGamePlay.gamePlayName == null || editGamePlay.gamePlayName == '') {
				layer.alert('请输入玩法名称', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGamePlay.oddsMode == null || editGamePlay.oddsMode == '') {
				layer.alert('请选择赔率模式', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGamePlay.oddsMode == '1' && (editGamePlay.odds == null || editGamePlay.odds == '')) {
				layer.alert('请输入赔率', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGamePlay.gamePlayCategoryName == null || editGamePlay.gamePlayCategoryName == '') {
				layer.alert('请输入玩法类别', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGamePlay.subGamePlayCategoryName == null || editGamePlay.subGamePlayCategoryName == '') {
				layer.alert('请输入子玩法类别', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGamePlay.state == null || editGamePlay.state == '') {
				layer.alert('请设置状态', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGamePlay.gamePlayDesc == null || editGamePlay.gamePlayDesc == '') {
				layer.alert('请输入玩法说明', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var numLocates = editGamePlay.numLocates;
			for (var i = 0; i < numLocates.length; i++) {
				var numLocate = numLocates[i];
				if (numLocate.numLocateName == null || numLocate.numLocateName == '') {
					layer.alert('请输入号位', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (numLocate.nums == null || numLocate.nums == '') {
					layer.alert('请输入可选号球', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (numLocate.maxSelected == null || numLocate.maxSelected == '') {
					layer.alert('请输入最大可选数量', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (numLocate.hasFilterBtnFlag == null) {
					layer.alert('请选择是否显示快捷按钮', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}

				if (editGamePlay.oddsMode == '1') {
					that.generateNum(numLocate, editGamePlay.odds);
				}
				if (editGamePlay.oddsMode == '2') {
					var optionalNums = numLocate.optionalNums;
					if (optionalNums.length == 0) {
						layer.alert('请点击号码明细下拉菜单设置各个号码的赔率', {
							title : '提示',
							icon : 7,
							time : 3000
						});
						return;
					}
					for (var j = 0; j < optionalNums.length; j++) {
						var optionalNum = optionalNums[j];
						if (optionalNum.odds == null || optionalNum.odds == '') {
							layer.alert('请设置各个号码的赔率', {
								title : '提示',
								icon : 7,
								time : 3000
							});
							return;
						}
					}
				}
			}

			that.$http.post('/game/addOrUpdateGamePlay', editGamePlay).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addOrUpdateGamePlayFlag = false;
				that.selectedGamePlay = {};
				that.loadGamePlayAndInitTree();
			});

		},
	}
});