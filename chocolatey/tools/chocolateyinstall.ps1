$version = '2.5.8'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'E7344FD480B2E2DB625A0A14049758229EDA725BF93645409DBAC05C7A8A7E5E'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
