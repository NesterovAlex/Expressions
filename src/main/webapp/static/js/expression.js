$('document').ready(function () {
    console.log("it works")
    $('.table .edit').on('click', function (event) {
        event.preventDefault();
        const href = $(this).attr('href');
        const modal = $('editModal');

        $.get(href, function (expression, status) {
            $('.editForm #id').val(expression.id)
            $('.editForm #expression').val(expression.expression)
            $('#resultEdit').val(expression.result)
        });

        $('#editModal').modal();
    });
});