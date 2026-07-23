import { z as zod } from 'zod';

export const DateVOSchema = zod.object({
  date: zod.date().optional().nullable(),
  localDateTime: zod.lazy(() => LocalDateTimeSchema.optional().nullable()),
  localDate: zod.lazy(() => LocalDateSchema.optional().nullable()),
  localTime: zod.lazy(() => LocalTimeSchema.optional().nullable()),
  localTime2: zod.lazy(() => LocalTimeSchema.optional().nullable()),
});

export const DefGenTestSimpleSaveVOSchema = zod.object({
  name: zod.string().min(1, { message: '请填写名称' }).max(24, { message: '名称长度不能超过{max}' }),
  stock: zod.number().int(),
  type: zod.enum(['ORDINARY', 'GIFT']).optional().nullable(),
  type2: zod.enum(['ORDINARY', 'GIFT']).optional().nullable(),
  type3: zod.string().max(255, { message: '学历长度不能超过{max}' }).optional().nullable(),
  state: zod.boolean().optional().nullable(),
  test4: zod.number().int().optional().nullable(),
  test5: zod.lazy(() => LocalDateSchema.optional().nullable()),
  test6: zod.lazy(() => LocalDateTimeSchema.optional().nullable()),
  parentId: zod.number().int().optional().nullable(),
  label: zod.string().max(255, { message: '名称长度不能超过{max}' }).optional().nullable(),
  sortValue: zod.number().int().optional().nullable(),
  test7: zod.string().max(10, { message: '字符字典长度不能超过{max}' }).optional().nullable(),
  test12: zod.number().int().optional().nullable(),
  userId: zod.number().int().optional().nullable(),
  orgId: zod.number().int().optional().nullable(),
  test8: zod.number().optional().nullable(),
  test9: zod.number().optional().nullable(),
  test10: zod.number().optional().nullable(),
  test11: zod.number().optional().nullable(),
});

export const DefGenTestSimpleSaveVOBuilderSchema = zod.object({});

export const DefGenTestTreeSaveVOSchema = zod.object({
  parentId: zod.number().int().optional().nullable(),
  sortValue: zod.number().int().optional().nullable(),
  name: zod.string().min(1, { message: '请填写名称' }).max(24, { message: '名称长度不能超过{max}' }),
  stock: zod.number().int(),
  type: zod.enum(['ORDINARY', 'GIFT']).optional().nullable(),
  type2: zod.enum(['ORDINARY', 'GIFT']).optional().nullable(),
  type3: zod.string().max(255, { message: '学历长度不能超过{max}' }).optional().nullable(),
  state: zod.boolean().optional().nullable(),
  test4: zod.number().int().optional().nullable(),
  test5: zod.lazy(() => LocalDateSchema.optional().nullable()),
  test6: zod.lazy(() => LocalDateTimeSchema.optional().nullable()),
  label: zod.string().max(255, { message: '名称长度不能超过{max}' }).optional().nullable(),
  test7: zod.string().max(10, { message: '字符字典长度不能超过{max}' }).optional().nullable(),
  test12: zod.number().int().optional().nullable(),
  userId: zod.number().int().optional().nullable(),
  orgId: zod.number().int().optional().nullable(),
  test8: zod.number().optional().nullable(),
  test9: zod.number().optional().nullable(),
  test10: zod.number().optional().nullable(),
  test11: zod.number().optional().nullable(),
});

export const DefGenTestTreeSaveVOBuilderSchema = zod.object({});

export const FormValidatorSaveVOSchema = zod.object({
  assertFalse2: zod.boolean(),
  assertTrue2: zod.boolean(),
  name: zod.string().min(1, { message: '请填写名称' }).max(24, { message: '名称长度不能超过{max}' }),
  name2: zod.string().min(1, { message: '请填写名称2' }).min(4).max(24, { message: '名称长度必须介于{min}-{max}之间' }),
  notBlank: zod.string().regex(/\S/, { message: '不能为空' }),
  notNull: zod.number().int(),
  notNullLong: zod.number().int(),
  null2: zod.number().int().optional().nullable(),
  decimalMax: zod.number().optional().nullable(),
  decimalMin: zod.number().optional().nullable(),
  digits: zod.number().optional().nullable(),
  max: zod.number().max(12, { message: '不能超过{value}' }).optional().nullable(),
  min: zod.number().min(2, { message: '不能小于{value}' }).optional().nullable(),
  email: zod.string().email({ message: '请输入正确的邮箱' }).optional().nullable(),
  futureDateTime: zod.lazy(() => LocalDateTimeSchema.optional().nullable()),
  futureLocalDate: zod.lazy(() => LocalDateSchema.optional().nullable()),
  futureDate: zod.date().optional().nullable(),
  futureOrPresentDateTime: zod.lazy(() => LocalDateTimeSchema.optional().nullable()),
  pastDateTime: zod.lazy(() => LocalDateTimeSchema.optional().nullable()),
  pastOrPresentDateTime: zod.lazy(() => LocalDateTimeSchema.optional().nullable()),
  negative: zod.number().int().negative({ message: '必须小于0' }).optional().nullable(),
  negativeOrZero: zod.number().int().max(0, { message: '必须小于等于0' }).optional().nullable(),
  positive: zod.number().int().positive({ message: '必须大于0' }).optional().nullable(),
  positiveOrZero: zod.number().int().min(0, { message: '必须大于等于0' }).optional().nullable(),
  mobile: zod.string().min(1, { message: '请填写登录手机号' }).max(11, { message: '登录手机号长度不能超过{max}' }),
  mobile2: zod.string().regex(/^(?:0|86|\+86)?1[3-9]\d{9}$/, { message: '请输入11位合法的手机号' }).optional().nullable(),
});

export const SerializeVOSchema = zod.object({
  localDateTime: zod.lazy(() => LocalDateTimeSchema.optional().nullable()),
  date: zod.date().optional().nullable(),
  localDate: zod.lazy(() => LocalDateSchema.optional().nullable()),
  localTime: zod.lazy(() => LocalTimeSchema.optional().nullable()),
  lon: zod.number().int().optional().nullable(),
  bi: zod.number().int().optional().nullable(),
  bd: zod.number().optional().nullable(),
  sex: zod.enum(['M', 'W']).optional().nullable(),
});

export const DefGenTestSimpleUpdateVOSchema = zod.object({
  id: zod.number().int(),
  name: zod.string().min(1, { message: '请填写名称' }).max(24, { message: '名称长度不能超过{max}' }),
  stock: zod.number().int(),
  type: zod.enum(['ORDINARY', 'GIFT']).optional().nullable(),
  type2: zod.enum(['ORDINARY', 'GIFT']).optional().nullable(),
  type3: zod.string().max(255, { message: '学历长度不能超过{max}' }).optional().nullable(),
  state: zod.boolean().optional().nullable(),
  test4: zod.number().int().optional().nullable(),
  test5: zod.lazy(() => LocalDateSchema.optional().nullable()),
  test6: zod.lazy(() => LocalDateTimeSchema.optional().nullable()),
  parentId: zod.number().int().optional().nullable(),
  label: zod.string().max(255, { message: '名称长度不能超过{max}' }).optional().nullable(),
  sortValue: zod.number().int().optional().nullable(),
  test7: zod.string().max(10, { message: '字符字典长度不能超过{max}' }).optional().nullable(),
  test12: zod.number().int().optional().nullable(),
  userId: zod.number().int().optional().nullable(),
  orgId: zod.number().int().optional().nullable(),
  test8: zod.number().optional().nullable(),
  test9: zod.number().optional().nullable(),
  test10: zod.number().optional().nullable(),
  test11: zod.number().optional().nullable(),
});

export const DefGenTestSimpleUpdateVOBuilderSchema = zod.object({});

export const DefGenTestTreeUpdateVOSchema = zod.object({
  parentId: zod.number().int().optional().nullable(),
  sortValue: zod.number().int().optional().nullable(),
  id: zod.number().int(),
  name: zod.string().min(1, { message: '请填写名称' }).max(24, { message: '名称长度不能超过{max}' }),
  stock: zod.number().int(),
  type: zod.enum(['ORDINARY', 'GIFT']).optional().nullable(),
  type2: zod.enum(['ORDINARY', 'GIFT']).optional().nullable(),
  type3: zod.string().max(255, { message: '学历长度不能超过{max}' }).optional().nullable(),
  state: zod.boolean().optional().nullable(),
  test4: zod.number().int().optional().nullable(),
  test5: zod.lazy(() => LocalDateSchema.optional().nullable()),
  test6: zod.lazy(() => LocalDateTimeSchema.optional().nullable()),
  label: zod.string().max(255, { message: '名称长度不能超过{max}' }).optional().nullable(),
  test7: zod.string().max(10, { message: '字符字典长度不能超过{max}' }).optional().nullable(),
  test12: zod.number().int().optional().nullable(),
  userId: zod.number().int().optional().nullable(),
  orgId: zod.number().int().optional().nullable(),
  test8: zod.number().optional().nullable(),
  test9: zod.number().optional().nullable(),
  test10: zod.number().optional().nullable(),
  test11: zod.number().optional().nullable(),
});

export const DefGenTestTreeUpdateVOBuilderSchema = zod.object({});

export const SerializableSchema = zod.object({});
