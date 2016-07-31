'use strict';

angular.module('aclcrawlerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('password_history', {
                parent: 'entity',
                url: '/password_historys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Password_historys'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/password_history/password_historys.html',
                        controller: 'Password_historyController'
                    }
                },
                resolve: {
                }
            })
            .state('password_history.detail', {
                parent: 'entity',
                url: '/password_history/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Password_history'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/password_history/password_history-detail.html',
                        controller: 'Password_historyDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Password_history', function($stateParams, Password_history) {
                        return Password_history.get({password_history_id : $stateParams.password_history_id});
                    }]
                }
            })
            .state('password_history.new', {
                parent: 'password_history',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/password_history/password_history-dialog.html',
                        controller: 'Password_historyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    user_id: null,
                                    password: null,
                                    create_date: null,
                                    password_history_id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('password_history', null, { reload: true });
                    }, function() {
                        $state.go('password_history');
                    })
                }]
            })
            .state('password_history.edit', {
                parent: 'password_history',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/password_history/password_history-dialog.html',
                        controller: 'Password_historyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Password_history', function(Password_history) {
                                return Password_history.get({password_history_id : $stateParams.password_history_id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('password_history', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
