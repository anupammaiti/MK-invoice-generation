document.addEventListener('DOMContentLoaded', function () {
    let form = $('#myForm');

    document.getElementById('btn').addEventListener('click', function () {
        $(form).validate({
            rules:{
                id: {
                    required: true
                }
            }
        });
        if(form.valid()){
            let id = document.getElementById('selectedInvoice').value;
            window.location.href = "/find/update/"+id;
        }
    });
});