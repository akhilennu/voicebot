import { element, by, ElementFinder } from 'protractor';
import { waitUntilDisplayed, waitUntilHidden, isVisible } from '../../util/utils';

const expect = chai.expect;

export default class NEREntityUpdatePage {
  pageTitle: ElementFinder = element(by.id('voicebotApp.nEREntity.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  entityNameInput: ElementFinder = element(by.css('input#ner-entity-entityName'));
  startInput: ElementFinder = element(by.css('input#ner-entity-start'));
  endInput: ElementFinder = element(by.css('input#ner-entity-end'));
  utteranceSelect: ElementFinder = element(by.css('select#ner-entity-utterance'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setEntityNameInput(entityName) {
    await this.entityNameInput.sendKeys(entityName);
  }

  async getEntityNameInput() {
    return this.entityNameInput.getAttribute('value');
  }

  async setStartInput(start) {
    await this.startInput.sendKeys(start);
  }

  async getStartInput() {
    return this.startInput.getAttribute('value');
  }

  async setEndInput(end) {
    await this.endInput.sendKeys(end);
  }

  async getEndInput() {
    return this.endInput.getAttribute('value');
  }

  async utteranceSelectLastOption() {
    await this.utteranceSelect.all(by.tagName('option')).last().click();
  }

  async utteranceSelectOption(option) {
    await this.utteranceSelect.sendKeys(option);
  }

  getUtteranceSelect() {
    return this.utteranceSelect;
  }

  async getUtteranceSelectedOption() {
    return this.utteranceSelect.element(by.css('option:checked')).getText();
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }

  async enterData() {
    await waitUntilDisplayed(this.saveButton);
    await this.setEntityNameInput('entityName');
    await waitUntilDisplayed(this.saveButton);
    await this.setStartInput('5');
    await waitUntilDisplayed(this.saveButton);
    await this.setEndInput('5');
    await this.utteranceSelectLastOption();
    await this.save();
    await waitUntilHidden(this.saveButton);
  }
}
