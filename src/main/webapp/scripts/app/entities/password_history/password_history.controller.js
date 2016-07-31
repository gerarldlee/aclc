'use strict';

angular.module('aclcrawlerApp')
    .controller('Password_historyController', function ($scope, Password_history) {
        $scope.password_historys = [];
        $scope.loadAll = function() {
            Password_history.query(function(result) {
               $scope.password_historys = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Password_history.get({password_history_id: id}, function(result) {
                $scope.password_history = result;
                $('#deletePassword_historyConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Password_history.delete({password_history_id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePassword_historyConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.password_history = {
                user_id: null,
                password: null,
                create_date: null,
                password_history_id: null
            };
        };
    });
