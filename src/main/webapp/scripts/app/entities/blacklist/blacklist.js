'use strict';

angular.module('aclcrawlerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('blacklist', {
                parent: 'entity',
                url: '/blacklists',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Blacklists'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/blacklist/blacklists.html',
                        controller: 'BlacklistController'
                    }
                },
                resolve: {
                }
            })
            .state('blacklist.detail', {
                parent: 'entity',
                url: '/blacklist/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Blacklist'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/blacklist/blacklist-detail.html',
                        controller: 'BlacklistDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Blacklist', function($stateParams, Blacklist) {
                        return Blacklist.get({id : $stateParams.id});
                    }]
                }
            })
            .state('blacklist.new', {
                parent: 'blacklist',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/blacklist/blacklist-dialog.html',
                        controller: 'BlacklistDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    blacklist_id: null,
                                    partial_qualified_name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('blacklist', null, { reload: true });
                    }, function() {
                        $state.go('blacklist');
                    })
                }]
            })
            .state('blacklist.edit', {
                parent: 'blacklist',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/blacklist/blacklist-dialog.html',
                        controller: 'BlacklistDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Blacklist', function(Blacklist) {
                                return Blacklist.get({blacklist_id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('blacklist', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
