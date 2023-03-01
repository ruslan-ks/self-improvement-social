function handleLanguageSelectChange() {
    // Reload the page with a language parameter set
    const localeSelect = document.querySelector('#localeSelect');
    const parser = new URL(window.location);
    parser.searchParams.set('lang', localeSelect.value);
    window.location = parser.href;
}