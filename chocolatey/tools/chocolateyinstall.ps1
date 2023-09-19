$version = '4.1.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'B4BA69FF75E0BA3A82C87EBF52C03B61D25F3BAA631D43E2E2BE0D9B4B197D40'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
