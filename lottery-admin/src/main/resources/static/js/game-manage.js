var gameManage = new Vue({
	el : '#game-manage',
	data : {
		games : [],
		editGame : {},
		addOrUpdateGameFlag : false,
		gameActionTitle : '',
		issueSettingFlag : false,
		issueSettingDetails : {},
		showGameManageFlag : true,
		selectedGame : {},
		selectedGamePlay : {},
		editGamePlay : {},
		addOrUpdateGamePlayFlag : false,
		gamePlayActionTitle : '',
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.initGameManageTable();
	},
	methods : {

		/**
		 * 初始化表格
		 */
		initGameManageTable : function() {
			var that = this;
			that.$http.get('/game/findAllGame', {}).then(function(res) {
				that.games = res.body.data;
			});

		},

		openIssueSettingModal : function(game) {
			var that = this;
			that.issueSettingFlag = true;
			that.$http.get('/issue/getIssueSettingDetailsByGameCode', {
				params : {
					gameCode : game.gameCode
				}
			}).then(function(res) {
				if (res.body.data != null) {
					that.issueSettingDetails = res.body.data;
				} else {
					that.issueSettingDetails = {
						gameCode : game.gameCode,
						dateFormat : '',
						issueFormat : '',
						issueGenerateRules : []
					};
				}
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

		openAddGameModal : function() {
			this.addOrUpdateGameFlag = true;
			this.gameActionTitle = '新增游戏';
			this.editGame = {
				gameName : '',
				gameCode : '',
				gameDesc : '',
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
			that.addOrUpdateGameFlag = true;
			that.gameActionTitle = '编辑游戏';
			that.$http.get('/game/findGameById', {
				params : {
					id : game.id
				}
			}).then(function(res) {
				that.editGame = res.body.data;
			});
		},

		/**
		 * 新增或修改游戏
		 */
		addOrUpdateGame : function() {
			var that = this;
			var editGame = that.editGame;
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

		backToGameManage : function() {
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
				that.editGamePlay = res.body.data;
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
				hasFilterBtnFlag : true
			});
		},

		delNumLocate : function(index) {
			this.editGamePlay.numLocates.splice(index, 1);
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
			if (editGamePlay.odds == null || editGamePlay.odds == '') {
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
				if (numLocate.hasFilterBtnFlag == null || numLocate.hasFilterBtnFlag == '') {
					layer.alert('请选择是否显示快捷按钮', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
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