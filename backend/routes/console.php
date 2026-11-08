<?php

use Illuminate\Support\Facades\Artisan;

Artisan::command('inspire', function () {
    $this->comment('Build useful things.');
})->purpose('Display an inspirational quote');
