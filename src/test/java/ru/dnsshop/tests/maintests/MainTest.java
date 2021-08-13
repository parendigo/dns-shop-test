package ru.dnsshop.tests.maintests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.dnsshop.tests.base.BaseTests;

import java.util.ArrayList;
import java.util.List;

public class MainTest extends BaseTests {
    private List<String> prices = new ArrayList<String>();
    private List<String> warranties = new ArrayList<String>();
    private int basketSumm = 0;

    @Test
    public void mainTest () {

        clickElement(By.xpath("//div[contains(@class,'city-select')]//a[text()='Да']"));

        // 1 В поиске находим "монитор Samsung" и кликаем на поиск
        clickWaitAndFillField(By.xpath("//form[contains(@action,'search')]//" +
                "input[(contains(@placeholder,'Поиск по сайту'))and not(@id)]"), "Монитор Samsung");
        clickElement(By.xpath("//nav[@id='header-search']//" +
                "span[(contains(@class,'input-search')) and not(contains(@class,'clear'))]"));

        // 2 кликаем на "Монитор Samsung S24F354FHI черный"
        clickElement(By.xpath("//div[contains(@class,'products-list__content')]//a[contains(@class,'catalog-product')]" +
                "//span[contains(.,'23.5\" Монитор Samsung S24F354FHI черный')]"));

        // 3 Запоминаем цену
        prices.add(getElementAttribute(By.xpath("//div[contains(@class,'product-buy__price-wrap')]" +
                "//div[contains(@class,'product-buy__price') " +
                "or contains(@class,'product-buy__price product-buy__price_active')]"), "innerHTML"));

        // 4 Выбираем гарантию
        clickElement(By.xpath("//div[contains(@class,'product-card-top')]" +
                "//div[@class='additional-sales-tabs__titles-wrap']//div[contains(.,'Гарантия')]"));
        clickElement(By.xpath("//div[contains(@class,'product-card-top')]" +
                "//div[@class='additional-sales-tabs__content-wrap']//label[contains(@class,'product-warranty')]" +
                "//span[contains(.,'+ 24')]"));
        warranties.add(getElementAttribute(By.xpath("//span[contains(.,'+ 24')]/" +
                "ancestor::label[@class='ui-radio__item product-warranty__item']//span[@class='product-warranty__price']"), "innerHTML"));

        // 5 Проверяем изменение цены и записываем новую цену
        getWait().until(ExpectedConditions.not(ExpectedConditions.textToBe(By.xpath("" +
                "//div[contains(@class,'product-buy__price-wrap')]//div[contains(@class,'product-buy__price') " +
                "or contains(@class,'product-buy__price product-buy__price_active')]"), prices.get(0))));

        // 6 кликаем купить
        clickElement(By.xpath("//div[contains(@class,'product-buy product-buy_one-line')]/button[contains(.,'Купить')]"));

        // 7 Ищем Detroit
        clickAndFillField(By.xpath("//form[contains(@action,'search')]//" +
                "input[(contains(@placeholder,'Поиск по сайту'))and not(@id)]"), "Detroit");
        clickElement(By.xpath("//nav[@id='header-search']//" +
                "span[(contains(@class,'input-search')) and not(contains(@class,'clear'))]"));

        // 8 Запоминаем цену Detroit, выбрано самое первое из списка
        prices.add(getElementAttribute(By.xpath("//div[contains(@class,'products-list__content')]" +
                "/div[1]//div[@class='product-buy__price']"), "innerHTML"));

        // 9 Жмем купить
        String tmpBasketPrice = getElementAttribute(By.xpath("//nav[@id='header-search']//span[@class='cart-link__price']"), "innerHTML");
        clickElement(By.xpath("//div[contains(@class,'products-list__content')]/div[1]//button[contains(.,'Купить')]"));

        // 10 Проверяем, что цена корзины стала равна сумме покупок
        getWait().until(ExpectedConditions.not(ExpectedConditions.textToBe(By.xpath("" +
                "//nav[@id='header-search']//span[@class='cart-link__price']"), tmpBasketPrice)));
        tmpBasketPrice = getElementAttribute(By.xpath("//nav[@id='header-search']//span[@class='cart-link__price']"), "innerHTML");
        if ((convertStringToInt(prices.get(0)) + convertStringToInt(prices.get(1)) + convertStringToInt(warranties.get(0))) !=
                convertStringToInt(getElementAttribute(By.xpath("//nav[@id='header-search']//span[@class='cart-link__price']"), "innerHTML")))
            Assertions.fail("Summ of ordered items is not equal to basket summ");

        // 11 Переходим в корзину
        clickElement(By.xpath("//nav[@id='header-search']//span[@class='cart-link__price']"));

        // 12 Проверяем, что для монитора выбрана гарантия
        checkExistance(By.xpath("//a[contains(.,'23.5\" Монитор Samsung S24F354FHI черный')]/ancestor::div" +
                "[@class='cart-items__product']//span[@class='base-ui-radio-button__icon base-ui-radio-button__icon_checked' and contains(.,'+ 24')]"));

        // 13 Проверяем соответсвтие цены каждого из товаров
        if (convertStringToInt(getElementAttribute(By.xpath("//a[contains(.,'23.5\" Монитор Samsung S24F354FHI черный')]" +
                "/ancestor::div[@class='cart-items__product']//span[@class='price__current']"), "innerHTML")) != convertStringToInt(prices.get(0)) ||
        convertStringToInt(getElementAttribute(By.xpath("//a[contains(.,'Игра Detroit: Стать человеком (PS4)')]" +
                "/ancestor::div[@class='cart-items__product']//span[@class='price__current']"), "innerHTML")) != convertStringToInt(prices.get(1)))
            Assertions.fail("Price is not the same compare to price, searched previously");

        // 14 Удиляем из корзины Detroit
        tmpBasketPrice = getElementAttribute(By.xpath("//nav[@id='header-search']//span[@class='cart-link__price']"), "innerHTML");
        clickElement(By.xpath("//a[contains(.,'Игра Detroit: Стать человеком (PS4)')]" +
                "/ancestor::div[@class='cart-items__product']//i[@class='count-buttons__icon-minus']"));

        // 15 Проверяем что Detroit нет больше в корзине и сумма уменьшилась
        getWait().until(ExpectedConditions.not(ExpectedConditions.textToBe(By.xpath("" +
                "//nav[@id='header-search']//span[@class='cart-link__price']"), tmpBasketPrice)));
        if (checkExistance(By.xpath("//a[contains(.,'Игра Detroit: Стать человеком (PS4)')]")) != null)
            Assertions.fail("Detroit was not deleted from basket");
        if (convertStringToInt(prices.get(0)) + convertStringToInt(warranties.get(0)) !=
                convertStringToInt(getElementAttribute(By.xpath("//nav[@id='header-search']//span[@class='cart-link__price']"), "innerHTML")))
            Assertions.fail("After deleting Detroit from basket summ is not correct");

        // 16 Добавляем еще 2 монитора Самсунг кнопкой "+" и проверяем сумму
        tmpBasketPrice = getElementAttribute(By.xpath("//nav[@id='header-search']//span[@class='cart-link__price']"), "innerHTML");
        clickElement(By.xpath("//a[contains(.,'23.5\" Монитор Samsung S24F354FHI черный')]" +
                "/ancestor::div[@class='cart-items__product']//i[@class='count-buttons__icon-plus']"));
        getWait().until(ExpectedConditions.not(ExpectedConditions.textToBe(By.xpath("" +
                "//nav[@id='header-search']//span[@class='cart-link__price']"), tmpBasketPrice)));
        tmpBasketPrice = getElementAttribute(By.xpath("//nav[@id='header-search']//span[@class='cart-link__price']"), "innerHTML");
        clickElement(By.xpath("//a[contains(.,'23.5\" Монитор Samsung S24F354FHI черный')]" +
                "/ancestor::div[@class='cart-items__product']//i[@class='count-buttons__icon-plus']"));
        getWait().until(ExpectedConditions.not(ExpectedConditions.textToBe(By.xpath("" +
                "//nav[@id='header-search']//span[@class='cart-link__price']"), tmpBasketPrice)));
        tmpBasketPrice = getElementAttribute(By.xpath("//nav[@id='header-search']//span[@class='cart-link__price']"), "innerHTML");

        if (convertStringToInt(getElementAttribute(By.xpath("//nav[@id='header-search']" +
                "//span[@class='cart-link__price']"), "innerHTML")) != (3 * (convertStringToInt(prices.get(0)) + convertStringToInt(warranties.get(0)))))
            Assertions.fail("After adding 2 additional monitors summ was not correct");

        // 17 Возвращаем Detroit обратно и проверяем, что сумма товаров увеличилась снова
        basketSumm = convertStringToInt(getElementAttribute(By.xpath("//nav[@id='header-search']//span[@class='cart-link__price']"), "innerHTML"));
        tmpBasketPrice = getElementAttribute(By.xpath("//nav[@id='header-search']//span[@class='cart-link__price']"), "innerHTML");
        clickElement(By.xpath("//div[@class='cart-page-new']//span[contains(@class,'group-tabs-menu')]//span[contains(.,'Вернуть удалённый товар')]"));
        getWait().until(ExpectedConditions.not(ExpectedConditions.textToBe(By.xpath("" +
                "//nav[@id='header-search']//span[@class='cart-link__price']"), tmpBasketPrice)));
        if (basketSumm + convertStringToInt(prices.get(1)) != convertStringToInt(getElementAttribute(By.xpath("" +
                "//nav[@id='header-search']//span[@class='cart-link__price']"), "innerHTML")))
            Assertions.fail("After returning Detroit price summ is not correct");

    }

    public int convertStringToInt (String str) {
        int i = Integer.parseInt(str.replaceAll("\\D+",""));
        return i;
    }
}
