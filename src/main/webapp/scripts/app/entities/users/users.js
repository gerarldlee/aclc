'use strict';

angular.module('aclcrawlerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('users', {
                parent: 'entity',
                url: '/userss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Userss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/users/userss.html',
                        controller: 'UsersController'
                    }
                },
                resolve: {
                }
            })
            .state('users.detail', {
                parent: 'entity',
                url: '/users/{user_id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Users'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/users/users-detail.html',
                        controller: 'UsersDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Users', function($stateParams, Users) {
                        return Users.get({id : $stateParams.id});
                    }]
                }
            })
            .state('users.new', {
                parent: 'users',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/users/users-dialog.html',
                        controller: 'UsersDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    surname: null,
                                    email: null,
                                    password: null,
                                    status: null,
                                    create_by: null,
                                    create_date: null,
                                    modify_by: null,
                                    modify_date: null,
                                    user_id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('users', null, { reload: true });
                    }, function() {
                        $state.go('users');
                    })
                }]
            })
            .state('users.edit', {
                parent: 'users',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/users/users-dialog.html',
                        controller: 'UsersDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Users', function(Users) {
                                return Users.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('users', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
