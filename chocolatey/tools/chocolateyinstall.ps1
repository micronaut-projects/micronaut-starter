$version = '2.2.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'FB481DB8881C676145858054930F6EBE37F365EF1289A7B8DC4850A02065CDA2'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
