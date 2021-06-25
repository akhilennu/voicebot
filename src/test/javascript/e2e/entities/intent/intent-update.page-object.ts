import { element, by, ElementFinder } from 'protractor';
import { waitUntilDisplayed, waitUntilHidden, isVisible } from '../../util/utils';

const expect = chai.expect;

export default class IntentUpdatePage {
  pageTitle: ElementFinder = element(by.id('voicebotApp.intent.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  intentInput: ElementFinder = element(by.css('input#intent-intent'));
  intentNameInput: ElementFinder = element(by.css('input#intent-intentName'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setIntentInput(intent) {
    await this.intentInput.sendKeys(intent);
  }

  async getIntentInput() {
    return this.intentInput.getAttribute('value');
  }

  async setIntentNameInput(intentName) {
    await this.intentNameInput.sendKeys(intentName);
  }

  async getIntentNameInput() {
    return this.intentNameInput.getAttribute('value');
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
    await this.setIntentInput('intent');
    await waitUntilDisplayed(this.saveButton);
    await this.setIntentNameInput('intentName');
    await this.save();
    await waitUntilHidden(this.saveButton);
  }
}
