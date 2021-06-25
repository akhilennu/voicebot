import { element, by, ElementFinder } from 'protractor';
import { waitUntilDisplayed, waitUntilHidden, isVisible } from '../../util/utils';

const expect = chai.expect;

export default class TranscriptsUpdatePage {
  pageTitle: ElementFinder = element(by.id('voicebotApp.transcripts.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  utteranceInput: ElementFinder = element(by.css('input#transcripts-utterance'));
  intentInput: ElementFinder = element(by.css('input#transcripts-intent'));
  confidenceInput: ElementFinder = element(by.css('input#transcripts-confidence'));
  gcResponseInput: ElementFinder = element(by.css('input#transcripts-gcResponse'));
  entityResponseInput: ElementFinder = element(by.css('input#transcripts-entityResponse'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setUtteranceInput(utterance) {
    await this.utteranceInput.sendKeys(utterance);
  }

  async getUtteranceInput() {
    return this.utteranceInput.getAttribute('value');
  }

  async setIntentInput(intent) {
    await this.intentInput.sendKeys(intent);
  }

  async getIntentInput() {
    return this.intentInput.getAttribute('value');
  }

  async setConfidenceInput(confidence) {
    await this.confidenceInput.sendKeys(confidence);
  }

  async getConfidenceInput() {
    return this.confidenceInput.getAttribute('value');
  }

  async setGcResponseInput(gcResponse) {
    await this.gcResponseInput.sendKeys(gcResponse);
  }

  async getGcResponseInput() {
    return this.gcResponseInput.getAttribute('value');
  }

  async setEntityResponseInput(entityResponse) {
    await this.entityResponseInput.sendKeys(entityResponse);
  }

  async getEntityResponseInput() {
    return this.entityResponseInput.getAttribute('value');
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
    await this.setUtteranceInput('utterance');
    await waitUntilDisplayed(this.saveButton);
    await this.setIntentInput('intent');
    await waitUntilDisplayed(this.saveButton);
    await this.setConfidenceInput('confidence');
    await waitUntilDisplayed(this.saveButton);
    await this.setGcResponseInput('gcResponse');
    await waitUntilDisplayed(this.saveButton);
    await this.setEntityResponseInput('entityResponse');
    await this.save();
    await waitUntilHidden(this.saveButton);
  }
}
